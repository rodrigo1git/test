package com.prototype.socialNetwork.config;

import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {

        return args -> {
            // Verificamos si ya existe para no duplicarlo
            if (profileRepository.findByEmail("admin") == null) {

                Profile admin = new Profile();
                admin.setName("Super");
                admin.setLastName("User");
                admin.setPublicName("AdminMaster");
                admin.setEmail("admin");
                // IMPORTANTE: Encriptamos la contraseña aquí
                admin.setPassword(passwordEncoder.encode("admin"));

                // Si tienes un campo de ROL, aquí se lo pondrías:
                // admin.setRole("ROLE_ADMIN");

                profileRepository.save(admin);
                System.out.println("✅ USUARIO ADMIN CREADO: admin / admin");
            }
        };
    }
}