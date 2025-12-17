package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.config.UserPrincipal;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceJpa implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos directo (sin Optional)
        Profile profile = profileRepository.findByEmail(email);

        // 2. Verificamos si es null
        if (profile == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        // 3. Si no es null, convertimos y retornamos
        return new UserPrincipal(profile);
    }
}