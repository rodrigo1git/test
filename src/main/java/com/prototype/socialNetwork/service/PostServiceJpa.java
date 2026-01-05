package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.*;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceJpa implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmbeddingModel embeddingModel;

    private static final float CATEGORY_CONFIDENCE_THRESHOLD = 0.7f;
    private static final Integer GENERAL_CATEGORY_ID = 4;



    // Método Mapper: Sin llamar a repositorios externos
    public PostResponseDTO postResponseDTOMapping(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getPostId());
        dto.setTitle(post.getPostTitle());
        dto.setBody(post.getPostBody());
        dto.setImageUrl(post.getImageUrl());
        dto.setDateTime(post.getPostDate());

        dto.setAutorName(post.getProfile().getPublicName());
        dto.setProfileId(post.getProfile().getId());

        dto.setCategoryName(post.getCategory().getName());
        dto.setCategoryId(post.getCategory().getCategoryId());
        dto.setLikeCount(post.getLikeCount());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPosts() {
        List<Post> posts = postRepository.findAll();

        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post post : posts){
            dtos.add(postResponseDTOMapping(post));
        }
        return dtos;
    }



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
        // Esto refina la búsqueda: "¿A qué se parece este post comparado con otros posts reales?"
        List<SimilarCategoryDTO> similarPosts = postRepository.findNearestNeighborCategories(embedVector, ids);

        // 🔍 DEBUG — observar similitudes reales (temporal)
        // DEBUG TEMPORAL — inspeccionar similitudes
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

        return postResponseDTOMapping(postGuardado);
    }

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

        return postResponseDTOMapping(postGuardado);
    }

    @Override
    public List<PostResponseDTO> getRecommendedPosts(RecommendRequestDTO request) {
        List<Post> posts = postRepository.recommendPosts(request.getVector(), request.getId());
        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post p: posts){
            dtos.add(postResponseDTOMapping(p));
        }
        return dtos;
    }


    @Override
    public void deletePost(Integer id){
        postRepository.deleteById(id);
    }

    @Override
    public List<PostResponseDTO> getPostsByProfileId(Integer id){
        List<Post> posts = postRepository.getPostsByProfileId(id);
        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post p: posts){
            dtos.add(postResponseDTOMapping(p));
        }
        return dtos;
    }


    @Override
    public List<PostResponseDTO> getPostsByCategory(Integer id){
        List<Post> posts = postRepository.findPostByCategory(id);
        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post p: posts){
            dtos.add(postResponseDTOMapping(p));
        }
        return dtos;
    }

    @Override
    public List<PostResponseDTO> getPostsByFollowerId(Integer id) {
        List<Post> posts = postRepository.findPostByFollowerId(id);
        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post p: posts){
            dtos.add(postResponseDTOMapping(p));
        }
        return dtos;
    }

    @Override
    public PostResponseDTO getPostById(Integer id){
        Post p = postRepository.getReferenceById(id);
        return postResponseDTOMapping(p);
    }





}
