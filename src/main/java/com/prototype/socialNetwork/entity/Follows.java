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
    private FollowsId id = new FollowsId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    @JoinColumn(name = "id_follower")
    private Profile follower;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followedId")
    @JoinColumn(name = "id_followed")
    private Profile followed;

    @Column(name = "since", nullable = false)
    private LocalDateTime since;


    public Follows(Profile follower, Profile followed) {
        this.follower = follower;
        this.followed = followed;
        this.id = new FollowsId(follower.getId(), followed.getId());
        this.since = LocalDateTime.now();
    }
}