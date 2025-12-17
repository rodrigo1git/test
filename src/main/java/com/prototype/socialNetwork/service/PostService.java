package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostRequestDTO;
import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    public List<PostResponseDTO> getPosts();

    public PostResponseDTO insertPost(PostRequestDTO request);

    public void deletePost(Integer id);

    public List<PostResponseDTO> getPostsByProfileId(Integer id);

    public PostResponseDTO postResponseDTOMapping(Post post);

    public List<PostResponseDTO> getPostsByCategory(Integer id);

}
