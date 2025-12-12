package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
