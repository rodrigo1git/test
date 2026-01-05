package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.PostCategoryUpdateDTO;
import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.service.MinioService;
import com.prototype.socialNetwork.service.PostCategoryService;
import com.prototype.socialNetwork.service.PostService;
import lombok.RequiredArgsConstructor;
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
    private static final float CATEGORY_CONFIDENCE_THRESHOLD = 0.55f;



    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts(){
        return ResponseEntity.ok(postService.getPosts());
    }

    //solo json
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> insertPost(@RequestBody PostRequestDTO request) {
        // El servicio ya devuelve el DTO listo, no hay que mapear nada aquí
        PostResponseDTO response = postService.insertPost(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //json+imagen
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> insertPostWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") PostRequestDTO request // Spring convierte el JSON string a DTO automáticamente
    ) {
        // 1. Subir la imagen a MinIO y obtener URL
        String imageUrl = minioService.uploadImage(file);

        // 2. Inyectamos la URL en el DTO
        request.setImageUrl(imageUrl);

        PostResponseDTO response = postService.insertPostManual(request); //---------SACAR MANUAL PARA VOLVER A AUTOMATIZAR CATEGORIAS-----------------------------------------

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


    // Agregamos el endpoint para buscar por perfil que corregimos en el servicio
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByProfile(@PathVariable Integer profileId) {
        return ResponseEntity.ok(postService.getPostsByProfileId(profileId));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostByCategory(@PathVariable Integer id){
        return ResponseEntity.ok(postService.getPostsByCategory(id));
    }

    @GetMapping("/postByFollowed/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostByFollowerId(@PathVariable Integer id){
        return ResponseEntity.ok(postService.getPostsByFollowerId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }





}