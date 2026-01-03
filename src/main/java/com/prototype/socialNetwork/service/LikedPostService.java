package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;

import java.util.List;

public interface LikedPostService {

    public List<LikedPostResponseDTO> getLikes();

    public LikedPostResponseDTO insertLike(LikedPostRequestDTO request);

    public void dislikePost(LikedPostRequestDTO request);

    public List<LikedPostResponseDTO> getLikesById(Integer id);

    public void likePost(Integer profileId, Integer postId);

}
