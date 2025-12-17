package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT c FROM PostComment c WHERE c.post.postId = :postId")
    List<PostComment> findByPostId(Integer postId);

    // Busca por el ID del objeto 'profile' dentro del comentario
    List<PostComment> findByProfileId(Integer profileId);
}