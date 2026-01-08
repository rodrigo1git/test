package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.PostCategoryUpdateDTO;
import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.dto.RecommendRequestDTO;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.service.MinioService;
import com.prototype.socialNetwork.service.PostCategoryService;
import com.prototype.socialNetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MinioService minioService;

    //solo json
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> insertPost(@RequestBody PostRequestDTO request) {
        PostResponseDTO response = postService.insertPost(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //json+imagen
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> insertPostWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") PostRequestDTO request
    ) {

        String imageUrl = minioService.uploadImage(file);

        request.setImageUrl(imageUrl);

        PostResponseDTO response = postService.insertPost(request); //---------SACAR MANUAL PARA VOLVER A AUTOMATIZAR CATEGORIAS-----------------------------------------

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // BÚSQUEDAS
    // ==========================================


    // 1. PERFIL: Agrega viewerId
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByProfile(
            @PathVariable Integer profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer viewerId // <--- AGREGAR ESTO
    ) {
        // Pasar viewerId al servicio
        // Solución rápida: Si es null, asignar un valor por defecto (ej. -1 o 0)
        int safeViewerId = (viewerId != null) ? viewerId : 0;
        return ResponseEntity.ok(postService.getPostsByProfileId(profileId, safeViewerId, page));
    }

    // 2. CATEGORÍA: Agrega viewerId
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostByCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer viewerId // <--- AGREGAR ESTO
    ) {
        return ResponseEntity.ok(postService.getPostsByCategory(id, page, viewerId)); // Ajusta tu servicio para recibir page y viewerId
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer viewerId // <--- AGREGAR ESTO
    ) {
        return ResponseEntity.ok(postService.getPostById(id, viewerId));
    }

    // 4. SEGUIDOS: Agrega viewerId
    @GetMapping("/postByFollowed/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostByFollowerId(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer viewerId // <--- AGREGAR ESTO
    ){
        // Nota: Aquí el 'id' del path suele ser el usuario, así que viewerId es redundante
        // pero es buena práctica tenerlo separado si la lógica lo requiere.
        return ResponseEntity.ok(postService.getPostsByFollowerId(id, viewerId, page));
    }

    @GetMapping("/recommend/{id}")
    public ResponseEntity<List<PostResponseDTO>> recommendPosts(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page){
        return new ResponseEntity<>(postService.getRecommendedPosts(id,page), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Slice<PostResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer viewerId) {
        return ResponseEntity.ok(postService.getPosts(viewerId, page));
    }





}