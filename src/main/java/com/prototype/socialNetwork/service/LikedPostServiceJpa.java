package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.entity.LikedPostId;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.LikedPostRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import com.prototype.socialNetwork.utils.VectorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikedPostServiceJpa implements LikedPostService{

    private final LikedPostRepository likedPostRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final VectorUtils vectorUtils;

    @Override
    public List<LikedPostResponseDTO> getLikes() {
        List<LikedPost> likes = likedPostRepository.findAll();
        List<LikedPostResponseDTO> dtos = new ArrayList<>();
        for(LikedPost l: likes){
            dtos.add(mapToResponse(l));
        }
        return dtos;
    }

    @Override
    @Transactional
    public LikedPostResponseDTO insertLike(LikedPostRequestDTO request) {

        // 2. Traer entidades completas (necesitamos los vectores)
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post no existe"));

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new RuntimeException("Perfil no existe"));

        // 3. Calcular el nuevo vector del usuario (Lógica Two-Tower simplificada)
        long currentLikes = likedPostRepository.likeCount(profile.getId());

        float[] newPreferences = vectorUtils.calculateNewAverage(
                profile.getUserEmbedding(), // Vector actual (puede ser null)
                post.getEmbedding(),        // Vector del post
                currentLikes                // Cantidad de likes antes de este
        );

        // 4. Actualizar Perfil
        profile.setUserEmbedding(newPreferences);
        profileRepository.save(profile);

        // 5. Guardar la relación LikedPost (para historial y evitar duplicados)
        LikedPostId id = new LikedPostId(request.getPostId(), request.getProfileId());
        LikedPost likedPost = new LikedPost();
        likedPost.setId(id);
        likedPost.setPost(post);
        likedPost.setProfile(profile);
        likedPost.setLikedDate(LocalDateTime.now());

        LikedPost saved = likedPostRepository.save(likedPost);
        return mapToResponse(saved);
    }


    /*
    @Transactional
    @Override
    public LikedPostResponseDTO insertLike(LikedPostRequestDTO request) {
        // 1. Obtener "Proxies" (Referencias ligeras, sin query a DB todavía)
        Post postRef = postRepository.getReferenceById(request.getPostId());
        Profile profileRef = profileRepository.getReferenceById(request.getProfileId());

        // 2. Crear la entidad Like
        LikedPost likedPost = new LikedPost();

        // 3. Crear el ID Compuesto
        LikedPostId id = new LikedPostId(request.getPostId(), request.getProfileId());
        likedPost.setId(id);



        likedPost.setPost(postRef);
        likedPost.setProfile(profileRef);
        likedPost.setLikedDate(LocalDateTime.now());

        LikedPost saved = likedPostRepository.save(likedPost);
        postRepository.incrementLikeCount(request.getPostId());

        return mapToResponse(saved);
    }

     */

    @Override
    public void dislikePost(LikedPostRequestDTO request) {
        LikedPostId id = new LikedPostId();
        id.setProfileId(request.getProfileId());
        id.setPostId(request.getPostId());
        likedPostRepository.deleteById(id);
    }

    @Override
    public List<LikedPostResponseDTO> getLikesById(Integer id) {
        List<LikedPost> likes = likedPostRepository.getByProfileId(id);
        List<LikedPostResponseDTO> dtos = new ArrayList<>();
        for(LikedPost l: likes){
            dtos.add(mapToResponse(l));
        }
        return dtos;
    }

    @Transactional
    @Override
    public void likePost(Integer profileId, Integer postId) {
        int inserted = likedPostRepository.insertIfNotExists(profileId, postId);

        if (inserted == 1) {
            postRepository.incrementLikeCount(postId);
        }
    }

    private LikedPostResponseDTO mapToResponse(LikedPost post){
        return new LikedPostResponseDTO(post.getId().getPostId(), post.getId().getProfileId(), post.getLikedDate());
    }

}
