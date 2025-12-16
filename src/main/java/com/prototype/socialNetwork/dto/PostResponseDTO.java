package com.prototype.socialNetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO { // Es buena práctica ponerle el sufijo DTO

    // 1. EL CAMPO FALTANTE CRÍTICO
    private Integer id;

    // 2. Nombres simplificados (sin el prefijo 'post')
    private String title;
    private String body;

    // 3. URLs e imágenes
    private String imageUrl;

    // 4. Datos del Autor (Flattened)
    private Integer profileId;
    private String autorName; // 'autor' a veces es ambiguo, 'autorName' es claro

    // 5. Datos de Categoría
    private Integer categoryId;
    private String categoryName; // <--- Agregado: Muy útil para mostrar etiquetas en el front

    // 6. Metadatos
    private LocalDateTime dateTime;
}