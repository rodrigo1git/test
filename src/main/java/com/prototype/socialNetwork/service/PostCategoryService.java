package com.prototype.socialNetwork.service;


import com.prototype.socialNetwork.dto.PostCategoryUpdateDTO;
import com.prototype.socialNetwork.dto.SimilarCategoryDTO;
import com.prototype.socialNetwork.entity.PostCategory;

import java.util.List;

public interface PostCategoryService {

    List<PostCategory> getCategories();

    PostCategory insertCategory(String name, String description);

    List<SimilarCategoryDTO> findSimilarCategories(float[] embedVector);

    PostCategory updateCategoryDescription(PostCategoryUpdateDTO data);

}
