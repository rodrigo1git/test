package com.prototype.socialNetwork.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO (Data Transfer Object) para recibir datos de un nuevo Post desde el cliente.
 * Utiliza IDs simples para las claves foráneas (Profile y Category).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    // Campos del Post
    private String postTitle;
    private String postBody;

    // IDs de las claves foráneas
    private Integer profileId;      // ID del usuario que publica
    private Integer categoryId;     // ID de la categoría del post
    private String imageUrl; // Referencia URL a MinIO
}
