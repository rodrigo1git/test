package com.prototype.socialNetwork.config; // O com.prototype.socialNetwork.security

import com.prototype.socialNetwork.entity.Profile;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    // Composición: Guardamos el perfil dentro
    private final Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí defines la lógica de roles.
        // Si tu Profile tuviera un campo 'role', lo usarías aquí.
        // Por pureza, asignamos un rol por defecto.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return profile.getPassword(); // Delegamos al perfil
    }

    @Override
    public String getUsername() {
        return profile.getEmail(); // Delegamos al perfil (usamos email como usuario)
    }

    // Métodos de estado de cuenta (Return true = activa)
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // Opcional: Un getter para acceder al perfil real si lo necesitas luego
    public Profile getProfile() {
        return profile;
    }
}