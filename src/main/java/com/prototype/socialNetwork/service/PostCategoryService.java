package com.prototype.socialNetwork.service;


import com.prototype.socialNetwork.entity.PostCategory;

import java.util.List;

public interface PostCategoryService {

    List<PostCategory> getCategories();

    PostCategory insertCategory(String name);


}
