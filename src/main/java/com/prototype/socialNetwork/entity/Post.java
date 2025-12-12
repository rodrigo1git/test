package com.prototype.socialNetwork.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_title")
    private String postTitle;

    // "TEXT" en Postgres se mapea a String, pero es bueno explicitar la definición
    @Column(name = "post_body", nullable = false, columnDefinition = "TEXT")
    private String postBody;

    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    @Column(name = "image_url")
    private String imageUrl;

    // --- RELACIONES (Foreign Keys) ---

    // FK a Profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // FK a PostCategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;
}
