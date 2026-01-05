package com.prototype.socialNetwork.controller;


import com.prototype.socialNetwork.dto.PostCategoryUpdateDTO;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//HAY QUE AGREGAR METODO FINDBYID PARA PODER ENCONTRAR EL NOMBRE DE LA CATEGORIA FACILMENTE

@RestController
@RequestMapping("/api/post-category")
@CrossOrigin(origins = "*")
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
        return postCategoryService.insertCategory(postCategoryData.getName(), postCategoryData.getDescription());
    }



        @PutMapping("/description")
        public ResponseEntity<PostCategory> updateCategoryDescription(@RequestBody PostCategoryUpdateDTO request) {

            if (request.getId() == null) {
                return ResponseEntity.badRequest().build();
            }

            PostCategory updatedCategory = postCategoryService.updateCategoryDescription(request);

            return ResponseEntity.ok(updatedCategory);
        }
    }
