package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.service.LikedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/likedpost")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // Genera el constructor automáticamente para los campos final
public class LikedPostController {

    private final LikedPostService likedPostService;

    @GetMapping
    public ResponseEntity<List<LikedPostResponseDTO>> getLikes(){
        return ResponseEntity.ok(likedPostService.getLikes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LikedPostResponseDTO>> getLikesById(@PathVariable Integer id){
        return ResponseEntity.ok(likedPostService.getLikesById(id));
    }

    @PostMapping
    public ResponseEntity<LikedPostResponseDTO> insertLike(@RequestBody LikedPostRequestDTO request){
        LikedPostResponseDTO post = likedPostService.insertLike(request);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteLike(@RequestBody LikedPostRequestDTO request){
        likedPostService.dislikePost(request);
        return ResponseEntity.noContent().build();
    }

}
