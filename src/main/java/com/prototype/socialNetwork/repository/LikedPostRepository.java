package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.LikedPost;
import com.prototype.socialNetwork.entity.LikedPostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedPostRepository extends JpaRepository<LikedPost, LikedPostId> {

    @Query(value = """
            SELECT p.*
            FROM liked_post p
            WHERE p.profile_id = id
            """, nativeQuery = true)
    List<LikedPost> getByProfileId(@Param("id") Integer id);



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

    @Query(value = """
            SELECT COUNT(*)
            FROM liked_post p
            WHERE p.profile_id = id
            """, nativeQuery = true)
    int likeCount(@Param("id") Integer id);



}
