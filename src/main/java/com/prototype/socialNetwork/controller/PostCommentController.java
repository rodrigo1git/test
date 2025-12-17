package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.PostCommentRequestDTO;
import com.prototype.socialNetwork.dto.PostCommentResponseDTO;
import com.prototype.socialNetwork.service.MinioService; // Necesario para subir la foto
import com.prototype.socialNetwork.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Necesario para recibir el archivo

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final MinioService minioService; // ¡Inyectamos MinIO!

    // 1. Obtener todos
    @GetMapping
    public ResponseEntity<List<PostCommentResponseDTO>> getAllComments() {
        return ResponseEntity.ok(postCommentService.getAllComments());
    }

    // 2. Por Post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostCommentResponseDTO>> getCommentsByPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(postCommentService.getCommentsByPostId(postId));
    }

    // 3. Por Perfil
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<PostCommentResponseDTO>> getCommentsByProfile(@PathVariable Integer profileId) {
        return ResponseEntity.ok(postCommentService.getCommentsByProfileId(profileId));
    }

    // 4. Crear comentario SOLO TEXTO (JSON puro)
    @PostMapping
    public ResponseEntity<PostCommentResponseDTO> insertComment(@RequestBody PostCommentRequestDTO request) {
        // Pasamos null en la URL de imagen
        PostCommentResponseDTO response = postCommentService.insertComment(request, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ==========================================
    // 5. NUEVO: Crear comentario CON IMAGEN
    // ==========================================
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostCommentResponseDTO> insertCommentWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") PostCommentRequestDTO request
    ) {
        // 1. Subir a MinIO
        String imageUrl = minioService.uploadImage(file);

        // 2. Guardar comentario pasando la URL generada
        // Nota: El servicio recibe (DTO, StringUrl)
        PostCommentResponseDTO response = postCommentService.insertComment(request, imageUrl);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}