package com.prototype.socialNetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowsResponse {

    //private String followerName;
    //private String followedName;
    private LocalDateTime since;
    private Integer followerId;
    private Integer followedId;
}
