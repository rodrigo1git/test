package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.PostRequest;
import com.prototype.socialNetwork.dto.PostResponse;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.service.MinioService;
import com.prototype.socialNetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final MinioService minioService; // Inyectamos Minio

    @Autowired
    public PostController(PostService postService, PostRepository postRepository, MinioService minioService){
        this.postService = postService;
        this.minioService = minioService;
    }

    @GetMapping
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    // ==========================================
    // MÉTODO 1: ORIGINAL (Solo JSON)
    // URL: POST http://localhost:8080/api/post
    // ==========================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> insertPost(@RequestBody PostRequest request) {

        // Llamamos al servicio con los datos tal cual vienen
        Post newPost = postService.insertPost(
                request.getPostTitle(),
                request.getPostBody(),
                request.getProfileId(),
                request.getCategoryId(),
                request.getImageUrl() // Aquí la URL debe venir escrita en el JSON o ser null
        );

        return new ResponseEntity<>(mapToResponse(newPost), HttpStatus.CREATED);
    }

    // ==========================================
    // MÉTODO 2: NUEVO (Imagen + JSON)
    // URL: POST http://localhost:8080/api/post/upload
    // ==========================================
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> insertPostWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") PostRequest request
    ) {
        // 1. Subir la imagen a MinIO
        String imageUrl = minioService.uploadImage(file);

        // 2. Insertar el post usando la URL generada
        Post newPost = postService.insertPost(
                request.getPostTitle(),
                request.getPostBody(),
                request.getProfileId(),
                request.getCategoryId(),
                imageUrl // <--- Usamos la URL de MinIO
        );

        return new ResponseEntity<>(mapToResponse(newPost), HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para no repetir código de mapeo en ambos endpoints
    private PostResponse mapToResponse(Post newPost) {
        PostResponse response = new PostResponse();
        response.setPostTitle(newPost.getPostTitle());
        response.setPostBody(newPost.getPostBody());
        response.setImageUrl(newPost.getImageUrl());
        response.setProfileId(newPost.getProfile().getId());
        response.setCategoryId(newPost.getCategory().getCategoryId());

        String nombre = newPost.getProfile().getName() != null ? newPost.getProfile().getName() : "";
        String apellido = newPost.getProfile().getLastName() != null ? newPost.getProfile().getLastName() : "";
        response.setAutor(nombre + " " + apellido);

        response.setDateTime(newPost.getPostDate());
        return response;
    }
}