package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}