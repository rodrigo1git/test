
package com.prototype.socialNetwork.config;

import com.prototype.socialNetwork.service.UserDetailsServiceJpa; // Tu servicio purista
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Inyectamos al portero
    private final UserDetailsServiceJpa userDetailsServiceJpa; // Inyectamos la lógica de DB

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))

                // 1. GESTIÓN DE PERMISOS (Aquí defines quién entra y quién no)
                .authorizeHttpRequests(auth -> auth
                        // PÚBLICO: Login y Registro
                        .requestMatchers("/api/auth/**").permitAll()

                        // PÚBLICO: Ver posts y categorías (GET)
                        .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/postcategory/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()

                        // PRIVADO: Crear, Borrar, Editar (Cualquier otra cosa)
                        .anyRequest().authenticated()
                )

                // 2. SESIÓN STATELESS (Importante para JWT: No guardar cookies)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. PROVEEDOR DE AUTENTICACIÓN
                .authenticationProvider(authenticationProvider())

                // 4. AÑADIR NUESTRO FILTRO ANTES DEL DE SPRING
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para encriptar contraseñas (Igual que tenías antes)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que conecta UserDetailsService con PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsServiceJpa);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean para manejar el Login (lo usaremos en el AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuración CORS (Igual que tenías antes)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
