package com.prototype.socialNetwork.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FollowsId implements Serializable {

    // Estos nombres deben coincidir con los argumentos de @MapsId en la entidad Follows
    private Integer followerId;
    private Integer followedId;
}