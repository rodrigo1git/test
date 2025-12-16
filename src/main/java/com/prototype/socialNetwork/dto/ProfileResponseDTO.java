package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class ProfileResponseDTO {
    private Integer id;
    private String publicName;
    private String name;
    private String secondName;
    private String lastName;
    private String email;
}