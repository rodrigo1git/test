package com.prototype.socialNetwork.controller;


import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postcategory")
public class PostCategoryController {


    private final PostCategoryService postCategoryService;

    @Autowired
    public PostCategoryController(PostCategoryService postCategoryService){
        this.postCategoryService = postCategoryService;
    }

    @GetMapping
    public List<PostCategory> getPostCategories(){
        return postCategoryService.getCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostCategory insertPostCategory(@RequestBody PostCategory postCategoryData ){
        return postCategoryService.insertCategory(postCategoryData.getName());
    }

}
