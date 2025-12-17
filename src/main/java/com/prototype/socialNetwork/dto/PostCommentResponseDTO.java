package com.prototype.socialNetwork.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PostCommentResponseDTO {
    private Integer id;
    private String body;
    private String imageUrl;
    private LocalDate commentDate;

    // Devolvemos IDs y Nombres para facilitar la vista
    private Integer postId;
    private Integer authorId;
    private String authorName;
}