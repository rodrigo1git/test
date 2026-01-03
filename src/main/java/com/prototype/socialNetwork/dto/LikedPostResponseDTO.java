package com.prototype.socialNetwork.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedPostResponseDTO {
    private Integer postId;
    private Integer profileId;
    private LocalDateTime dateTime;
}
