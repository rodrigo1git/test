package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.dto.SimilarCategoryDTO;
import com.prototype.socialNetwork.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {


    @Query(value = """
            SELECT 
                category_id AS categoryId, 
                (1 - (embedding <=> cast(:vector as vector))) AS similarity
            FROM post_category
            ORDER BY embedding <=> cast(:vector as vector) ASC
            LIMIT 5
            """, nativeQuery = true)
    List<SimilarCategoryDTO> findSimilarCategories(@Param("vector") float[] vector);

    @Modifying
    @Query(value = """
    UPDATE post_category 
    SET description = :description, 
        embedding = cast(:embed as vector)
    WHERE category_id = :id
    """, nativeQuery = true)
    void updateCategoryDescription(
            @Param("id") Integer id,
            @Param("description") String description,
            @Param("embed") float[] embed
    );
}


