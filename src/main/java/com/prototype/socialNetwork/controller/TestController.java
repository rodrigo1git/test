package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.service.UserDetailsServiceJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/auth")
@RequiredArgsConstructor
public class TestController {

    private final UserDetailsServiceJpa userDetailsService;

    @GetMapping("/{email}")
    public UserDetails probarCargaUsuario(@PathVariable String email) {
        return userDetailsService.loadUserByUsername(email);
    }
}