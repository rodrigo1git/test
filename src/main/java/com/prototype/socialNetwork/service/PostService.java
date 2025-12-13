package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    public List<Post> getPosts();

    public Post insertPost(String title, String body, Integer profileId, Integer postCategoryId, String imageUrl);

    public void deletePost(Integer id);



}
