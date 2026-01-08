package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.dto.SimilarCategoryDTO;
import com.prototype.socialNetwork.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {



    @Query("SELECT p FROM Post p WHERE p.profile.id = :id ORDER BY p.postDate DESC")
    Slice<Post> getPostsByProfileId(@Param("id") Integer id, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category.categoryId = :id ORDER BY p.postDate DESC")
    Slice<Post> findPostByCategory(@Param("id") Integer id, Pageable pageable);

    @Query(value = """
    SELECT p.* FROM Post p 
    WHERE EXISTS (
        SELECT 1 FROM Follows f 
        WHERE f.id_follower = :id AND f.id_followed = p.profile_id
    )
    ORDER BY p.post_date DESC
    """,
            nativeQuery = true)
    Slice<Post> findPostByFollowerId(@Param("id") Integer userId, Pageable pageable);

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


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET like_count = like_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void incrementLikeCount(@Param("postId") Integer postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET like_count = like_count - 1 WHERE post_id = :postId", nativeQuery = true)
    void decrementLikeCount(@Param("postId") Integer postId);

    /*
    @Query(value = """
    SELECT p.* FROM post p
    WHERE :profileId IS NOT NULL OR :profileId IS NULL
    ORDER BY p.embedding <=> cast(:userVector as vector) ASC
    LIMIT 10
    """, nativeQuery = true)
    List<Post> recommendPosts(@Param("userVector") float[] userVector, @Param("profileId") Integer profileId);
     */


    @Query(value = """
    SELECT p.* FROM post p
    WHERE :profileId IS NOT NULL OR :profileId IS NULL
    ORDER BY p.embedding <=> cast(:userVector as vector) ASC
    """, nativeQuery = true)
    Slice<Post> recommendPosts(@Param("userVector") float[] userVector, @Param("profileId") Integer profileId, Pageable pageable);

    //WHERE p.post_id NOT IN (SELECT lp.post_id FROM liked_post lp WHERE lp.profile_id = :profileId) -- No mostrar posts ya likeados

    @Query("""
       SELECT new com.prototype.socialNetwork.dto.PostResponseDTO(
           p.postId,
           p.postTitle,
           p.postBody,
           p.imageUrl,
           p.profile.id,
           p.profile.name,
           p.category.id,
           p.category.name,
           p.postDate,
           p.likeCount,
           (CASE WHEN (EXISTS (
               SELECT 1 FROM LikedPost ml 
               WHERE ml.post = p AND ml.profile.id = :viewerId
           )) THEN true ELSE false END)
       )
       FROM Post p
       ORDER BY p.postDate DESC
       """)
    Slice<PostResponseDTO> getPosts(Integer viewerId, Pageable pageable);

}
