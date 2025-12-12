package com.prototype.socialNetwork.controller;


import com.prototype.socialNetwork.dto.PostRequest;
import com.prototype.socialNetwork.dto.PostResponse;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService, PostRepository postRepository){
        this.postService = postService;

    }

    @GetMapping
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    /*
    @PostMapping
    @ResponseStatus
    public Post insertPost(@RequestBody PostRequest postData){
        return postService.insertPost(postData.getPostTitle(), postData.getPostBody(), postData.getProfileId(), postData.getCategoryId(), postData.getImageUrl());
    }
     */

    @PostMapping
    public ResponseEntity<PostResponse> insertPost(@RequestBody PostRequest request) {

        // 1. Llamada al servicio (retorna la Entidad Post persistida)
        Post newPost = postService.insertPost(
                request.getPostTitle(),
                request.getPostBody(),
                request.getProfileId(),
                request.getCategoryId(),
                request.getImageUrl()
        );

        // 2. Mapeo: Convertir la Entidad (Post) al DTO de Respuesta (PostResponse)
        PostResponse response = new PostResponse();

        response.setPostTitle(newPost.getPostTitle());
        response.setPostBody(newPost.getPostBody());
        response.setImageUrl(newPost.getImageUrl());

        // Extraemos solo los IDs de las relaciones para evitar el bucle infinito
        response.setProfileId(newPost.getProfile().getId());
        response.setCategoryId(newPost.getCategory().getCategoryId());

        // Asignamos el nombre del autor (concatenando o usando el nombre)
        // Asumo que tu entidad Profile tiene getName() y getLastName()
        String nombreCompleto = newPost.getProfile().getName() + " " + newPost.getProfile().getLastName();
        response.setAutor(nombreCompleto);

        // Conversión de Fecha:
        // Si tu Entidad tiene LocalDate y el DTO tiene LocalDateTime:
        response.setDateTime(newPost.getPostDate());
        // Si ambos son LocalDate, simplemente: response.setDateTime(newPost.getPostDate());

        // 3. Retornar el DTO con estado 201 Created
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




}
