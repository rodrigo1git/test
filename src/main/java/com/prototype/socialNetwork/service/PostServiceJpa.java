package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.*;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.LikedPostRepository;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import com.prototype.socialNetwork.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceJpa implements PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final ProfileRepository profileRepository;
    private final LikedPostRepository likedPostRepository;
    private final Mapper mapper;
    private final int pageSize = 10;
    private static final Logger log = LoggerFactory.getLogger(PostServiceJpa.class);
    private final EmbeddingModel embeddingModel;
    private static final float CATEGORY_CONFIDENCE_THRESHOLD = 0.7f;
    private static final Integer GENERAL_CATEGORY_ID = 4;

    @Override
    @Transactional(readOnly = true)
    public Slice<PostResponseDTO> getPosts(Integer id, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return postRepository.getPosts(id, pageable);
    }

    @Override
    @Transactional
    public PostResponseDTO insertPost(PostRequestDTO request) {
        // 1. Generar Embedding (Operación costosa, hacemos esto primero)
        String contentToEmbed = request.getTitle() + " " + request.getBody();
        float[] embedVector = embeddingModel.embed(contentToEmbed);
        PostCategory resolvedCategory = resolveCategoryByEmbedding(embedVector);
        Post post = new Post();
        post.setPostTitle(request.getTitle());
        post.setPostBody(request.getBody());
        post.setImageUrl(request.getImageUrl());
        post.setPostDate(LocalDateTime.now());
        post.setEmbedding(embedVector);
        post.setLikeCount(0);
        post.setProfile(profileRepository.getReferenceById(request.getProfileId()));
        post.setCategory(resolvedCategory);
        return mapper.mapToResponse(postRepository.save(post));
    }

    private PostCategory resolveCategoryByEmbedding(float[] embedVector) {
        // A. Paso 1: Filtrar categorías candidatas (Broad Phase)
        List<SimilarCategoryDTO> candidateCategories = postCategoryRepository.findSimilarCategories(embedVector);

        // OPTIMIZACIÓN (Fail-fast): Si no hay categorías cercanas, retornamos General y ahorramos querys.
        if (candidateCategories.isEmpty()) {
            return postCategoryRepository.getReferenceById(GENERAL_CATEGORY_ID);
        }

        List<Integer> candidateIds = candidateCategories.stream()
                .map(SimilarCategoryDTO::getCategoryId)
                .toList(); // Java 16+ (o .collect(Collectors.toList()) en versiones viejas)

        // B. Paso 2: Buscar vecinos cercanos (Narrow Phase - KNN)
        List<SimilarCategoryDTO> neighbors = postRepository.findNearestNeighborCategories(embedVector, candidateIds);

        // C. Paso 3: Algoritmo de Votación (Majority Vote)
        Integer winningCategoryId = neighbors.stream()
                .filter(dto -> dto.getSimilarity() >= CATEGORY_CONFIDENCE_THRESHOLD)
                .collect(Collectors.groupingBy(SimilarCategoryDTO::getCategoryId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Gana la categoría con más vecinos cercanos
                .map(Map.Entry::getKey)
                .orElse(GENERAL_CATEGORY_ID); // Si nadie supera el umbral, asignamos General

        // Logueo debug (solo si está activo)
        if (log.isDebugEnabled()) {
            log.debug("Categoría resuelta: {} basada en {} vecinos.", winningCategoryId, neighbors.size());
        }

        return postCategoryRepository.getReferenceById(winningCategoryId);
    }

/*
    @Transactional
    @Override
    public PostResponseDTO insertPost(PostRequestDTO request) {
        // 1. Crear Entidad y Mapear datos simples
        Post post = new Post();
        post.setPostTitle(request.getTitle());
        post.setPostBody(request.getBody());
        post.setImageUrl(request.getImageUrl());
        post.setPostDate(LocalDateTime.now());

        // 2. Referencias
        Profile profile = profileRepository.getReferenceById(request.getProfileId());

        // 3. Generar Embedding (Vector)
        String contentToEmbed = request.getTitle() + " " + request.getBody();
        float[] embedVector = embeddingModel.embed(contentToEmbed);
        post.setEmbedding(embedVector);


        // A. Buscar las 5 categorías "conceptualmente" más cercanas
        List<SimilarCategoryDTO> categories = postCategoryRepository.findSimilarCategories(embedVector);

        // B. Extraer IDs de esas categorías candidatas
        List<Integer> ids = categories.stream()
                .map(SimilarCategoryDTO::getCategoryId)
                .collect(Collectors.toList());

        // C. Buscar los posts vecinos (K-Nearest Neighbors) pero SOLO dentro de esas categorías candidatas
        List<SimilarCategoryDTO> similarPosts = postRepository.findNearestNeighborCategories(embedVector, ids);

        // 🔍 DEBUG — observar similitudes reales
        for (SimilarCategoryDTO dto : similarPosts) {
            System.out.println(
                    "[CLASSIFIER] candidateCategory=" + dto.getCategoryId() +
                            " similarity=" + dto.getSimilarity()
            );
        }

        // D. Algoritmo de Votación por Mayoría (Majority Vote)
        Integer winningCategoryId = similarPosts.stream()
                // Solo dejamos votar a los vecinos que superen el umbral de similitud.
                .filter(dto -> dto.getSimilarity() >= CATEGORY_CONFIDENCE_THRESHOLD)

                // 2. Agrupar por Categoría y CONTAR cuántas veces aparece cada una
                .collect(Collectors.groupingBy(
                        SimilarCategoryDTO::getCategoryId,
                        Collectors.counting() // <--- CAMBIO CLAVE: Cuenta repeticiones
                ))

                // 3. Convertir el Mapa (ID -> Cantidad) a Stream para procesarlo
                .entrySet().stream()

                // 4. Elegir la entrada con el VALOR más alto (mayor cantidad de votos)
                .max(Map.Entry.comparingByValue())

                // 5. Extraer el ID de la categoría ganadora
                .map(Map.Entry::getKey)

                // 6. Si no hay vecinos o ninguno pasó el filtro, asignar General
                .orElse(GENERAL_CATEGORY_ID);

        // 5. Asignar Categoría Final y Guardar
        PostCategory postCategory = postCategoryRepository.getReferenceById(winningCategoryId);

        post.setProfile(profile);
        post.setCategory(postCategory);

        Post postGuardado = postRepository.save(post);

        return mapper.mapToResponse(postGuardado);
    }

     */

    @Override
    @Transactional
    public PostResponseDTO insertPostManual(PostRequestDTO request) {
        // 1. Crear Entidad y Mapear datos simples
        Post post = new Post();
        post.setPostTitle(request.getTitle());
        post.setPostBody(request.getBody());
        post.setImageUrl(request.getImageUrl());
        post.setPostDate(LocalDateTime.now());
        Profile profile = profileRepository.getReferenceById(request.getProfileId());

        Integer categoryId = request.getCategoryId();

        PostCategory postCategory = postCategoryRepository.getReferenceById(categoryId);

        String contentToEmbed = request.getTitle() + " " + request.getBody();
        float[] embedVector = embeddingModel.embed(contentToEmbed);
        post.setEmbedding(embedVector);

        // 5. Asignar relaciones y Guardar
        post.setProfile(profile);
        post.setCategory(postCategory);

        Post postGuardado = postRepository.save(post);

        return mapper.mapToResponse(postGuardado);
    }

    @Override
    public List<PostResponseDTO> getRecommendedPosts(Integer id, int page) {
        float[] vector = profileRepository.getReferenceById(id).getUserEmbedding();
        Slice<Post> slice = postRepository.recommendPosts(vector, id, PageRequest.of(page, pageSize));
        return mapper.mapPostsWithLikes(slice, id);
    }


    @Override
    public void deletePost(Integer id){
        postRepository.deleteById(id);
    }

    @Override
    public List<PostResponseDTO> getPostsByProfileId(Integer id, int page, Integer viewerId){
        Slice<Post> posts = postRepository.getPostsByProfileId(id, PageRequest.of(page, pageSize));
        return mapper.mapPostsWithLikes(posts, viewerId);

    }


    @Override
    public List<PostResponseDTO> getPostsByCategory(Integer id, int page, Integer viewerId){
        Slice<Post> posts = postRepository.findPostByCategory(id, PageRequest.of(page, pageSize));
        return mapper.mapPostsWithLikes(posts, viewerId);
    }

    @Override
    public List<PostResponseDTO> getPostsByFollowerId(Integer id, int page, Integer viewerId) {
        Slice<Post> posts = postRepository.findPostByFollowerId(id, PageRequest.of(page, pageSize));
        return mapper.mapPostsWithLikes(posts, viewerId);
    }


    @Override
    public PostResponseDTO getPostById(Integer id, Integer viewerId) {
        Post p = postRepository.getReferenceById(id);
        return mapper.mapPostWithLike(p, viewerId);
    }



}
