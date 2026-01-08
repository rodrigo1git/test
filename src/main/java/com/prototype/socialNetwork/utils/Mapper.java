package com.prototype.socialNetwork.utils;

import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.dto.ProfileResponseDTO;
import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.LikedPostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Component
public class Mapper {

    private final LikedPostRepository likedPostRepository;
    private final ProfileRepository profileRepository;

    public ProfileResponseDTO mapToResponse(Profile profile) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setPublicName(profile.getPublicName());
        dto.setName(profile.getName());
        dto.setSecondName(profile.getSecondName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getEmail());
        return dto;
    }

    public PostResponseDTO mapToResponse(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getPostId());
        dto.setTitle(post.getPostTitle());
        dto.setBody(post.getPostBody());
        dto.setImageUrl(post.getImageUrl());
        dto.setDateTime(post.getPostDate());

        dto.setAutorName(post.getProfile().getPublicName());
        dto.setProfileId(post.getProfile().getId());

        dto.setCategoryName(post.getCategory().getName());
        dto.setCategoryId(post.getCategory().getCategoryId());
        dto.setLikeCount(post.getLikeCount());
        return dto;
    }

    public List<PostResponseDTO> mapPostsWithLikes(Slice<Post> slice, Integer viewerId) {
        List<Post> posts = slice.getContent();
        if (posts.isEmpty()) return new ArrayList<>();
        // 1. Extraer IDs de los posts (Bucle For)
        List<Integer> postIds = new ArrayList<>(posts.size());
        for (Post p : posts) {
            postIds.add(p.getPostId());
        }
        // 2. Obtener IDs likeados desde la DB
        Set<Integer> likedIds;
        if (viewerId == null) {
            likedIds = Collections.emptySet();
        } else {
            likedIds = likedPostRepository.findLikesByUserIdAndPostIds(viewerId, postIds);
        }
        // 3. Mapear y asignar estado
        List<PostResponseDTO> dtos = new ArrayList<>(posts.size());
        for (Post post : posts) {
            PostResponseDTO dto = mapToResponse(post);
            if (likedIds.contains(post.getPostId())) {
                dto.setLikedByMe(true);
            }
            dtos.add(dto);
        }
        return dtos;
    }
    public LikedPostResponseDTO mapToResponse(LikedPost post){
        LikedPostResponseDTO dto = new LikedPostResponseDTO();
        return new LikedPostResponseDTO(post.getId().getPostId(), post.getId().getProfileId(), post.getLikedDate());
    }
    public List<LikedPostResponseDTO> mapLikeToResponse(List<LikedPost> likes){
        List<LikedPostResponseDTO> dtos = new ArrayList<>();
        for(LikedPost l: likes){
            dtos.add(mapToResponse(l));
        }
        return dtos;
    }
    public FollowsResponseDTO mapToResponse(Follows follows){
        FollowsResponseDTO response = new FollowsResponseDTO();
        response.setFollowedId(follows.getId().getFollowedId());
        response.setFollowerId(follows.getId().getFollowerId());
        response.setSince(follows.getSince());
        response.setFollowedName(profileRepository.getReferenceById(response.getFollowedId()).getPublicName());
        response.setFollowerName(profileRepository.getReferenceById(response.getFollowerId()).getPublicName());
        return response;
    }
    public List<FollowsResponseDTO> mapToResponse(List<Follows> follows){
        List<FollowsResponseDTO> dtos = new ArrayList<>();
        for(Follows f: follows){
            dtos.add(mapToResponse(f));
        }
        return dtos;
    }

    public PostResponseDTO mapPostWithLike(Post post, Integer viewerId) {
        // 1. Mapeo básico (Conversión de Entidad a DTO)
        PostResponseDTO dto = mapToResponse(post);

        // 2. Si hay un usuario mirando, verificamos si le dio like
        if (viewerId != null) {
            // Usamos un método eficiente que solo devuelve un booleano
            boolean isLiked = likedPostRepository.existsByProfile_IdAndPost_PostId(viewerId, post.getPostId());
            dto.setLikedByMe(isLiked);
        }

        return dto;
    }
}
