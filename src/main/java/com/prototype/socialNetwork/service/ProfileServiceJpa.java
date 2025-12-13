package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceJpa implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Profile> getProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    @Override
    public Profile insertProfile(String publicName, String name, String secondName, String lastName, String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        Profile p = new Profile();
        p.setPublicName(publicName);
        p.setName(name);
        p.setSecondName(secondName);
        p.setLastName(lastName);
        p.setEmail(email);
        p.setPassword(hashedPassword);
        return profileRepository.save(p);
    }

    @Override

    public void deleteProfile(Integer profileId){
        profileRepository.deleteById(profileId);
    }


}
