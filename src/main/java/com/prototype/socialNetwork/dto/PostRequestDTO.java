package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class PostRequestDTO {

    private String title;
    private String body;
    private Integer profileId;
    private Integer categoryId;
    private String imageUrl;

}