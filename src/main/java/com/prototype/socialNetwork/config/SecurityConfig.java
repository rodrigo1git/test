package com.prototype.socialNetwork.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
 @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. DESHABILITAR CSRF: Obligatorio para peticiones POST desde Postman sin tokens
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CONFIGURAR AUTORIZACIÓN DE RUTAS
                .authorizeHttpRequests(authorize -> authorize
                        // Si tu endpoint es /api/profiles, esta línea es vital:
                        //.requestMatchers("/api/profiles", "/api/profiles/**").permitAll() // <--- Permite acceso público

                        // Si quieres que todas las rutas /api/** estén abiertas temporalmente:
                        // .requestMatchers("/api/**").permitAll()

                        // Las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                // 3. Puedes deshabilitar por completo el login básico si no lo estás usando
                // .httpBasic(AbstractHttpConfigurer::disable);
                // O mantenerlo para futuros endpoints seguros
                .httpBasic(Customizer.withDefaults()) // Usar solo autenticación Basic Auth
                .formLogin(AbstractHttpConfigurer::disable) // <-- DESHABILITAR el formulario de login

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

 */
// NUEVO BEAN: Define usuarios para pruebas en memoria
@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails usuarioPrueba = User.builder()
            .username("admin") // Usuario para Postman
            .password(passwordEncoder.encode("12345")) // Contraseña hasheada correctamente
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(usuarioPrueba);
}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated() // Todo requiere autenticación
                )
                .httpBasic(Customizer.withDefaults()); // Habilita Basic Auth para Postman

        return http.build();
    }
}
