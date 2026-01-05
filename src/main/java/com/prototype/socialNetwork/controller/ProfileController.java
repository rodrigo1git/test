package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.ProfileRequestDTO;
import com.prototype.socialNetwork.dto.ProfileResponseDTO;
import com.prototype.socialNetwork.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> getProfiles(){
        return ResponseEntity.ok(profileService.getProfiles());
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> createProfile(@RequestBody ProfileRequestDTO request){
        ProfileResponseDTO newProfile = profileService.insertProfile(request);
        return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Integer id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> findProfile(@PathVariable Integer id){
        ProfileResponseDTO profile = profileService.findById(id);
        return ResponseEntity.ok(profile);
    }


}