package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {



    @Query("SELECT p FROM Post p WHERE p.profile.id = :id")
    public List<Post> getPostsByProfileId(@Param ("id") Integer id);

    @Query("SELECT p FROM Post p WHERE p.category.categoryId =: id")
    public List<Post> findPostByCategory(@Param("id") Integer id);

}
