package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.ProfileRequestDTO;
import com.prototype.socialNetwork.dto.ProfileResponseDTO;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
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

    // --- Mappers Privados ---

    private ProfileResponseDTO mapToResponse(Profile profile) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setPublicName(profile.getPublicName());
        dto.setName(profile.getName());
        dto.setSecondName(profile.getSecondName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getEmail());
        return dto;
    }

    private Profile mapToEntity(ProfileRequestDTO request) {
        Profile profile = new Profile();
        profile.setPublicName(request.getPublicName());
        profile.setName(request.getName());
        profile.setSecondName(request.getSecondName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        // IMPORTANTE: Aquí encriptamos la contraseña
        profile.setPassword(passwordEncoder.encode(request.getPassword()));
        return profile;
    }

    // --- Implementación de Métodos ---

    @Override
    @Transactional(readOnly = true)
    public List<ProfileResponseDTO> getProfiles() {
        return profileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfileResponseDTO insertProfile(ProfileRequestDTO request) {
        // 1. Convertir DTO a Entidad (incluye hashing de password)
        Profile profileToSave = mapToEntity(request);

        // 2. Guardar en DB
        Profile savedProfile = profileRepository.save(profileToSave);

        // 3. Devolver DTO de respuesta (sin password)
        return mapToResponse(savedProfile);
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
}