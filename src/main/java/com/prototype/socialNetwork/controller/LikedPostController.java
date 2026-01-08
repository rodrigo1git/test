package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.service.LikedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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

    /*
    @GetMapping("/{id}")
    public ResponseEntity<List<PostResponseDTO>> getLikesById(@PathVariable Integer id){
        return ResponseEntity.ok(likedPostService.getLikedPosts(id));
    }

     */

    @GetMapping("/{targetId}")
    public ResponseEntity<List<PostResponseDTO>> getLikesById(
            @PathVariable Integer targetId,
            @RequestParam(required = false) Integer viewerId,
            @RequestParam(defaultValue = "0") int page) {

        Slice<PostResponseDTO> slice = likedPostService.getLikedPosts(targetId, viewerId, page);

        // .getContent() extrae la Lista limpia del Slice
        return ResponseEntity.ok(slice.getContent());
    }

    @PostMapping
    public ResponseEntity<LikedPostResponseDTO> insertLike(@RequestBody LikedPostRequestDTO request){
        LikedPostResponseDTO post = likedPostService.insertLike(request);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<List<LikedPostResponseDTO>> insertLikeList(@RequestBody List<LikedPostRequestDTO> requests){
        List<LikedPostResponseDTO> posts = likedPostService.insertLike(requests);
        return new ResponseEntity<>(posts, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteLike(@RequestBody LikedPostRequestDTO request){
        likedPostService.dislikePost(request);
        return ResponseEntity.noContent().build();
    }

}
