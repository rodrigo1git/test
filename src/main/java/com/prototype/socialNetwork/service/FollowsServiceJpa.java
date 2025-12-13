package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.entity.FollowsId;
import com.prototype.socialNetwork.repository.FollowsRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FollowsServiceJpa implements FollowsService {

    @Autowired
    private FollowsRepository followsRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Override
    public List<Follows> getFollowers() {
        return followsRepository.findAll();
    }

    @Transactional
    @Override
    public Follows insertFollows(Integer followerId, Integer followedId) {
        FollowsId followsId = new FollowsId(followerId, followedId);
        Follows follows = new Follows(followsId, profileRepository.getReferenceById(followerId), profileRepository.getReferenceById(followedId), LocalDateTime.now());
        return followsRepository.save(follows);
    }
}
