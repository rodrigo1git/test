package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.Post;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostService {

    public Slice<PostResponseDTO> getPosts(Integer id, int page);

    public PostResponseDTO insertPost(PostRequestDTO request);

    public void deletePost(Integer id);

    public List<PostResponseDTO> getPostsByProfileId(Integer id, int page, Integer viewerId);

    public List<PostResponseDTO> getPostsByCategory(Integer id, int page, Integer viewerId);

    public List<PostResponseDTO> getPostsByFollowerId(Integer id, int page, Integer viewerId);

    public PostResponseDTO getPostById(Integer id, Integer viewerId);

    public PostResponseDTO insertPostManual(PostRequestDTO request);

    //public List<PostResponseDTO> getRecommendedPosts(Integer id);
    public List<PostResponseDTO> getRecommendedPosts(Integer id, int pageNumber);


}
