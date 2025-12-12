package com.prototype.socialNetwork.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data // Genera equals() y hashCode() obligatorios para claves compuestas
@NoArgsConstructor
@AllArgsConstructor
public class IsFriendToId implements Serializable {

    private Integer id1;
    private Integer id2;
}
