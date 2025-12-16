package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class ProfileRequestDTO {
    // Nota: El ID no se envía al crear


    private String publicName;

    private String name;

    private String secondName; // Opcional


    private String lastName;

    //@Email(message = "Debe ser un email válido")
    //@NotBlank(message = "El email es obligatorio")
    private String email;


    private String password; // Se recibe texto plano, el servicio lo hashea
}