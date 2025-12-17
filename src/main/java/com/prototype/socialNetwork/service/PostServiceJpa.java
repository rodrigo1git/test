package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponse;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceJpa implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private ProfileRepository profileRepository;

    // Método Mapper: Sin llamar a repositorios externos
    public PostResponseDTO postResponseDTOMapping(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getPostId());
        dto.setTitle(post.getPostTitle());
        dto.setBody(post.getPostBody());
        dto.setImageUrl(post.getImageUrl());
        dto.setDateTime(post.getPostDate());

        // USAR LAS RELACIONES YA EXISTENTES EN EL OBJETO

        dto.setAutorName(post.getProfile().getPublicName());
        dto.setProfileId(post.getProfile().getId());

        dto.setCategoryName(post.getCategory().getName());
        dto.setCategoryId(post.getCategory().getCategoryId());


        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Importante para mantener la sesión de DB abierta
    public List<PostResponseDTO> getPosts() {
        List<Post> posts = postRepository.findAll();

        // Opción Clásica (Corrigiendo el error de la variable 'p')
        List<PostResponseDTO> dtos = new ArrayList<>();
        for(Post post : posts){
            dtos.add(postResponseDTOMapping(post)); // Pasamos 'post', no 'posts'
        }
        return dtos;

        // Opción Moderna (Java Streams) - Recomendada
        // return posts.stream()
        //            .map(this::postResponseDTOMapping)
        //            .collect(Collectors.toList());
    }



    @Transactional
    @Override
    public PostResponseDTO insertPost(PostRequestDTO request) {
        // 1. Crear Entidad
        Post post = new Post();

        // 2. Mapear datos simples
        post.setPostTitle(request.getTitle());
        post.setPostBody(request.getBody());
        post.setImageUrl(request.getImageUrl());
        post.setPostDate(LocalDateTime.now());

        // 3. Referencias (Optimizadas con getReferenceById)
        Profile profile = profileRepository.getReferenceById(request.getProfileId());
        PostCategory postCategory = postCategoryRepository.getReferenceById(request.getCategoryId());

        post.setProfile(profile);
        post.setCategory(postCategory);

        // 4. Guardar
        Post postGuardado = postRepository.save(post);

        // 5. CRÍTICO: Devolver DTO, nunca la entidad
        return postResponseDTOMapping(postGuardado);
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
}
