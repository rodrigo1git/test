package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.dto.PostResponseDTO;
import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.entity.LikedPostId;
import com.prototype.socialNetwork.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface LikedPostRepository extends JpaRepository<LikedPost, LikedPostId> {

    @Query(value = """
            SELECT p.*
            FROM liked_post p
            WHERE p.profile_id = :id
            """, nativeQuery = true)
    List<LikedPost> getByProfileId(@Param("id") Integer id);


    @Query("SELECT lp.post FROM LikedPost lp WHERE lp.profile.id = :profileId ORDER BY lp.likedDate DESC")
    List<Post> getLikesById(@Param("profileId") Integer profileId);



    @Modifying
    @Query(value = """
    INSERT INTO liked_post (profile_id, post_id)
    VALUES (:profileId, :postId)
    ON CONFLICT DO NOTHING
""", nativeQuery = true)
    int insertIfNotExists(Integer profileId, Integer postId);

    @Modifying
    @Query("""
    DELETE FROM LikedPost lp
    WHERE lp.id.profileId = :profileId
      AND lp.id.postId = :postId
    """)
    int deleteIfExists(
            @Param("profileId") Integer profileId,
            @Param("postId") Integer postId
    );

    @Query(value = "SELECT COUNT(*) FROM liked_post WHERE profile_id = :profileId", nativeQuery = true)
    long likeCount(@Param("profileId") Integer profileId);

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
       FROM LikedPost lp
       JOIN lp.post p
       WHERE lp.profile.id = :targetId
       ORDER BY lp.likedDate DESC
       """)
    Slice<PostResponseDTO> getLikedPostsSlice(
            @Param("targetId") Integer targetId,
            @Param("viewerId") Integer viewerId,
            Pageable pageable
    );
    @Query("SELECT lp.post.postId FROM LikedPost lp WHERE lp.profile.id = :userId AND lp.post.postId IN :postIds")
    Set<Integer> findLikesByUserIdAndPostIds(@Param("userId") Integer userId, @Param("postIds") List<Integer> postIds);

    boolean existsByProfile_IdAndPost_PostId(Integer profileId, Integer postId);

}
