package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.entity.FollowsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowsRepository extends JpaRepository<Follows, FollowsId> {
}
