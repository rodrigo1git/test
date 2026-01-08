package com.prototype.socialNetwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostResponseDTO {

    private Integer id;
    private String title;
    private String body;
    private String imageUrl;
    private Integer profileId;
    private String autorName;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime dateTime;
    private Integer likeCount;
    private boolean likedByMe;

    public PostResponseDTO(Integer id, String title, String body, String imageUrl,
                           Integer profileId, String autorName,
                           Integer categoryId, String categoryName,
                           LocalDateTime dateTime, Integer likeCount, boolean likedByMe) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.profileId = profileId;
        this.autorName = autorName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.dateTime = dateTime;
        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
    }
}