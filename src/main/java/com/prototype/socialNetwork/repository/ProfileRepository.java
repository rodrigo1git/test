package com.prototype.socialNetwork.repository;

import com.prototype.socialNetwork.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p WHERE p.email = :email")
    Profile findByEmail(String email);
}
