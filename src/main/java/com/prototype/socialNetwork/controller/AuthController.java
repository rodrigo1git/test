package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.*;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
import com.prototype.socialNetwork.service.JwtService;
import com.prototype.socialNetwork.service.ProfileService;
import com.prototype.socialNetwork.service.UserDetailsServiceJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceJpa userDetailsService;

    // CAMBIO: Inyectamos el Servicio, NO el Repositorio ni el Encoder
    private final ProfileService profileService;

    // En AuthController.java

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody ProfileRequestDTO request) {
        ProfileResponseDTO user = profileService.insertProfile(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        // Pasamos token, nombre Y EL ID
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getName(), user.getId()));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        // Buscamos el perfil para sacar nombre e ID
        ProfileResponseDTO profile = profileService.findByEmail(request.getEmail());

        return ResponseEntity.ok(new AuthResponseDTO(token, profile.getName(), profile.getId()));
    }
}