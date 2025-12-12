package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCategoryServiceJpa implements PostCategoryService{

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Override
    public List<PostCategory> getCategories() {
        return postCategoryRepository.findAll();
    }

    @Transactional
    @Override
    public PostCategory insertCategory(String name) {
        PostCategory postCategory = new PostCategory();
        postCategory.setName(name);
        return postCategoryRepository.save(postCategory);
    }
}
