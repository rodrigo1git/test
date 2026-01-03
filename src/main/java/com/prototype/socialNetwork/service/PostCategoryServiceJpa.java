package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostCategoryUpdateDTO;
import com.prototype.socialNetwork.dto.SimilarCategoryDTO;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCategoryServiceJpa implements PostCategoryService{

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Override
    public List<PostCategory> getCategories() {
        return postCategoryRepository.findAll();
    }

    //
    //HAY QUE CREAR DATA TRANSFER OBJECT PARA POSTCATEGORY-------------------------------------------------------------------------------------------------------------------
    //

    @Transactional
    @Override
    public PostCategory insertCategory(String name, String description) {
        PostCategory postCategory = new PostCategory();
        postCategory.setName(name);
        postCategory.setDescription(description);
        postCategory.setEmbedding(embeddingModel.embed(description));
        return postCategoryRepository.save(postCategory);
    }

    @Override
    public List<SimilarCategoryDTO> findSimilarCategories(float[] embedVector) {
        return postCategoryRepository.findSimilarCategories(embedVector);
    }

    @Transactional
    @Override
    public PostCategory updateCategoryDescription(PostCategoryUpdateDTO data) {
        float[] embed = embeddingModel.embed(data.getDescription());
        postCategoryRepository.updateCategoryDescription(data.getId(), data.getDescription(), embed);
        return postCategoryRepository.getReferenceById(data.getId());
    }


}
