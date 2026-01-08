package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.entity.LikedPostId;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.LikedPostRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import com.prototype.socialNetwork.utils.Mapper;
import com.prototype.socialNetwork.utils.VectorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final Mapper mapper;

    @Override
    public List<LikedPostResponseDTO> getLikes() {
        return mapper.mapLikeToResponse(likedPostRepository.findAll());
    }

    @Override
    @Transactional
    public LikedPostResponseDTO insertLike(LikedPostRequestDTO request) {

        LikedPostId id = new LikedPostId(request.getPostId(), request.getProfileId());

        if (likedPostRepository.existsById(id)) {
            LikedPost existing = likedPostRepository.getReferenceById(id);
            return mapper.mapToResponse(existing); // 🟢 Devolvemos 200 OK con el existente
        }
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post no existe"));

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new RuntimeException("Perfil no existe"));

        // 3. Calcular el nuevo vector del usuario
        // AHORA ES SEGURO: Solo calculamos esto si es un like NUEVO
        long currentLikes = likedPostRepository.likeCount(profile.getId());

        float[] newPreferences = vectorUtils.calculateNewAverage(
                profile.getUserEmbedding(),
                post.getEmbedding(),
                currentLikes
        );

        profile.setUserEmbedding(newPreferences);
        profileRepository.save(profile);

        LikedPost likedPost = new LikedPost();
        likedPost.setId(id);
        likedPost.setPost(post);
        likedPost.setProfile(profile);
        likedPost.setLikedDate(LocalDateTime.now());

        postRepository.incrementLikeCount(request.getPostId());

        LikedPost saved = likedPostRepository.save(likedPost);
        return mapper.mapToResponse(saved);
    }


    @Override
    public void dislikePost(LikedPostRequestDTO request) {
        LikedPostId id = new LikedPostId();
        id.setProfileId(request.getProfileId());
        id.setPostId(request.getPostId());
        likedPostRepository.deleteById(id);
        postRepository.decrementLikeCount(request.getPostId());
    }

    @Override
    public List<LikedPostResponseDTO> getLikesById(Integer id) {
        return mapper.mapLikeToResponse(likedPostRepository.getByProfileId(id));
    }


    @Override
    public List<LikedPostResponseDTO> insertLike(List<LikedPostRequestDTO> likes) {
        List<LikedPostResponseDTO> dtos = new ArrayList<>();
        for(LikedPostRequestDTO like: likes){
            dtos.add(insertLike(like));
        }
        return dtos;
    }


    public Slice<PostResponseDTO> getLikedPosts(Integer targetId, Integer viewerId, int pageNumber) {
        Integer safeViewerId = (viewerId != null) ? viewerId : -1;
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return likedPostRepository.getLikedPostsSlice(targetId, safeViewerId, pageable);
    }
}
