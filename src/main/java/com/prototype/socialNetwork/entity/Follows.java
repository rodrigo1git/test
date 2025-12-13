package com.prototype.socialNetwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follows {

    @EmbeddedId
    private FollowsId id = new FollowsId(); // Inicializamos para evitar NullPointer

    // RELACIÓN 1: El que sigue (Follower)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId") // Vincula este objeto con el campo 'followerId' de FollowsId
    @JoinColumn(name = "id_follower") // Nombre real de la columna en la DB
    private Profile follower;

    // RELACIÓN 2: El seguido (Followed)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followedId") // Vincula este objeto con el campo 'followedId' de FollowsId
    @JoinColumn(name = "id_followed") // Nombre real de la columna en la DB
    private Profile followed;

    @Column(name = "since", nullable = false)
    private LocalDateTime since;

    // Constructor auxiliar para crear relaciones fácilmente
    public Follows(Profile follower, Profile followed) {
        this.follower = follower;
        this.followed = followed;
        this.id = new FollowsId(follower.getId(), followed.getId());
        this.since = LocalDateTime.now();
    }
}