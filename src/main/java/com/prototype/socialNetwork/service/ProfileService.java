package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Profile;

import java.util.List;

public interface ProfileService {

    public List<Profile> getProfiles();
    public Profile insertProfile(String name, String secondName, String lastName, String email, String password);

}
