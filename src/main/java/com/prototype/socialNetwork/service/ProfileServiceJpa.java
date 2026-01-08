package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.ProfileRequestDTO;
import com.prototype.socialNetwork.dto.ProfileResponseDTO;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
import com.prototype.socialNetwork.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Inyección de dependencias moderna
public class ProfileServiceJpa implements ProfileService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    private Profile mapToEntity(ProfileRequestDTO request) {
        Profile profile = new Profile();
        profile.setPublicName(request.getPublicName());
        profile.setName(request.getName());
        profile.setSecondName(request.getSecondName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPassword(passwordEncoder.encode(request.getPassword()));
        return profile;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProfileResponseDTO> getProfiles() {
        return profileRepository.findAll().stream()
                .map(mapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfileResponseDTO insertProfile(ProfileRequestDTO request) {
        Profile profileToSave = mapToEntity(request);
        Profile savedProfile = profileRepository.save(profileToSave);
        return mapper.mapToResponse(savedProfile);
    }

    @Override
    @Transactional
    public void deleteProfile(Integer profileId){
        // Buena práctica: Verificar si existe antes de borrar
        if (profileRepository.existsById(profileId)) {
            profileRepository.deleteById(profileId);
        } else {
            // Opcional: lanzar excepción o ignorar
            throw new RuntimeException("Perfil no encontrado");
        }
    }

    @Override
    public ProfileResponseDTO findByEmail(String email) {
        Profile profile = profileRepository.findByEmail(email);
        return mapper.mapToResponse(profile);
    }

    @Override
    public ProfileResponseDTO findById(Integer id){
        Profile p = profileRepository.getReferenceById(id);
        return mapper.mapToResponse(p);
    }


}