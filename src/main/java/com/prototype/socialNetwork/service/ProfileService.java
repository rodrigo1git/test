package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.ProfileRequestDTO;
import com.prototype.socialNetwork.dto.ProfileResponseDTO;
import com.prototype.socialNetwork.entity.Profile;

import java.util.List;

public interface ProfileService {

    List<ProfileResponseDTO> getProfiles();
    ProfileResponseDTO insertProfile(ProfileRequestDTO request);
    void deleteProfile(Integer profileId);

    //public Profile updatePassword(String OldPassword, String newPassword);
}
