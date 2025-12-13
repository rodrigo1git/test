package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Profile;

import java.util.List;

public interface ProfileService {

    public List<Profile> getProfiles();
    public Profile insertProfile(String publicName, String name, String secondName, String lastName, String email, String password);
    public void deleteProfile(Integer profileId);

    //public Profile updatePassword(String OldPassword, String newPassword);
}
