package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.LikedPostRequestDTO;
import com.prototype.socialNetwork.dto.LikedPostResponseDTO;
import com.prototype.socialNetwork.dto.PostResponse;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface LikedPostService {

    public List<LikedPostResponseDTO> getLikes();

    public LikedPostResponseDTO insertLike(LikedPostRequestDTO request);

    public void dislikePost(LikedPostRequestDTO request);

    public List<LikedPostResponseDTO> getLikesById(Integer id);

    //public List<PostResponseDTO> getLikedPosts(Integer id);
    public Slice<PostResponseDTO> getLikedPosts(Integer targetId, Integer viewerId, int pageNumber);

    public List<LikedPostResponseDTO> insertLike (List<LikedPostRequestDTO> likes);

}
