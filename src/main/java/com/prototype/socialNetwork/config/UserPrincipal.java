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


    private final Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // hay que definir roles
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public Profile getProfile() {
        return profile;
    }
}