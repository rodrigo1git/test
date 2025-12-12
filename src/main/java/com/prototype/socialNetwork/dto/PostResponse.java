package com.prototype.socialNetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String postTitle;
    private String postBody;

    // IDs de las claves foráneas
    private Integer profileId;      // ID del usuario que publica
    private Integer categoryId;     // ID de la categoría del post
    private String imageUrl; // Referencia URL a MinIO

    private String autor;
    private LocalDateTime dateTime;

}
