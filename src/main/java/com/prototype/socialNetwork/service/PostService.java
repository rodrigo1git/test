package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.dto.RecommendRequestDTO;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    public List<PostResponseDTO> getPosts();

    public PostResponseDTO insertPost(PostRequestDTO request);

    public void deletePost(Integer id);

    public List<PostResponseDTO> getPostsByProfileId(Integer id);

    public PostResponseDTO postResponseDTOMapping(Post post);

    public List<PostResponseDTO> getPostsByCategory(Integer id);

    public List<PostResponseDTO> getPostsByFollowerId(Integer id);

    public PostResponseDTO getPostById(Integer id);
    public PostResponseDTO insertPostManual(PostRequestDTO request);

    public List<PostResponseDTO> getRecommendedPosts(RecommendRequestDTO request);


}
