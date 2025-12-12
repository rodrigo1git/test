package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
