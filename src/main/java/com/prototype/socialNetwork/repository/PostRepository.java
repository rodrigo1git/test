package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.dto.SimilarCategoryDTO;
import com.prototype.socialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {



    @Query("SELECT p FROM Post p WHERE p.profile.id = :id")
    public List<Post> getPostsByProfileId(@Param ("id") Integer id);

    @Query("SELECT p FROM Post p WHERE p.category.categoryId = :id")
    public List<Post> findPostByCategory(@Param("id") Integer id);

    @Query(value = "SELECT p.* FROM Post p " +
                    "WHERE EXISTS (SELECT 1 FROM Follows f " +
                    "WHERE f.id_follower = :id AND f.id_followed = p.profile_id)",
            nativeQuery = true)
    public List<Post> findPostByFollowerId(@Param("id") Integer userId);

    @Query(value = """
        SELECT 
            p.category_id as categoryId, 
            (1 - (p.embedding <=> cast(:vector as vector))) as similarity
        FROM post p
        WHERE p.category_id IN (:categoryIds)
        ORDER BY p.embedding <=> cast(:vector as vector) ASC
        LIMIT 5
        """, nativeQuery = true)
    public List<SimilarCategoryDTO> findNearestNeighborCategories(
            @Param("vector") float[] vector,
            @Param("categoryIds") List<Integer> categoryIds
    );

    @Modifying
    @Query("""
    UPDATE Post p
    SET p.likeCount = p.likeCount + 1
    WHERE p.id = :postId
    """)
    int incrementLikeCount(@Param("postId") Integer postId);

    @Modifying
    @Query("""
    UPDATE Post p
    SET p.likeCount = p.likeCount - 1
    WHERE p.id = :postId AND p.likeCount > 0
""")
    void decrementLikeCount(@Param("postId") Integer postId);





}
