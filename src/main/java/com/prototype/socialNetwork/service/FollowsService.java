package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Follows;

import java.util.List;

public interface FollowsService {

    public List<Follows> getFollowers();

    public Follows insertFollows(Integer followerId, Integer followedId);
}
