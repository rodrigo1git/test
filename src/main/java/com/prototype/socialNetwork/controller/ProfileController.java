package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping
    public List<Profile> getProfiles(){
        return profileService.getProfiles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile createProfile(@RequestBody Profile profileData){
        return profileService.insertProfile(   profileData.getName(),
                                        profileData.getSecondName(),
                                        profileData.getLastName(),
                                        profileData.getEmail(),
                                        profileData.getPassword());
    }
}
