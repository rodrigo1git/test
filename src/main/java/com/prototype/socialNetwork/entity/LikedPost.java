package com.prototype.socialNetwork.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "liked_post")
@Data
public class LikedPost {

    @EmbeddedId
    private LikedPostId id;

    @ManyToOne
    @MapsId("postId") // Vincula con el ID dentro del Embeddable
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("profileId")
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private LocalDateTime likedDate;

}