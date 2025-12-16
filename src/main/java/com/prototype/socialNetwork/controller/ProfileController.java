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
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> getProfiles(){
        return ResponseEntity.ok(profileService.getProfiles());
    }

    @PostMapping
    // Sin @Valid: Spring no validará los campos automáticamente
    public ResponseEntity<ProfileResponseDTO> createProfile(@RequestBody ProfileRequestDTO request){
        ProfileResponseDTO newProfile = profileService.insertProfile(request);
        return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Integer id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}