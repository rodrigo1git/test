package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.entity.FollowsId;
import com.prototype.socialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowsRepository extends JpaRepository<Follows, FollowsId> {


    // 1. Ver a quién sigo (Mis "Seguidos")
    // SQL equiv: SELECT * FROM follows WHERE follower_id = :id
    List<Follows> findByFollowerId(Integer id);

    // 2. Ver quién me sigue (Mis "Seguidores")
    // SQL equiv: SELECT * FROM follows WHERE followed_id = :id
    List<Follows> findByFollowedId(Integer id);
}
