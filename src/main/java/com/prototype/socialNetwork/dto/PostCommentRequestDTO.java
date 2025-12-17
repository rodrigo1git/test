package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class PostCommentRequestDTO {
    private String body;
    private Integer postId;
    private Integer profileId; // El autor del comentario
    // La imagen se manejaría aparte si es Multipart, o aquí si es string
}