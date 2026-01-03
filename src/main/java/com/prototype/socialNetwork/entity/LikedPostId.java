package com.prototype.socialNetwork.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LikedPostId implements Serializable {
    private Integer postId;
    private Integer profileId;
    // Getters, Setters, equals() y hashCode() OBLIGATORIOS
}