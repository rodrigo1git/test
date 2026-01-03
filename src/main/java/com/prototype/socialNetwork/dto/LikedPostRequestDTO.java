package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class LikedPostRequestDTO {

    private Integer postId;
    private Integer profileId;

}
