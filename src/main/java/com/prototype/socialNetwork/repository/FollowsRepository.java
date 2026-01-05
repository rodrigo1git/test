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


    List<Follows> findByFollowerId(Integer id);

    List<Follows> findByFollowedId(Integer id);

}
