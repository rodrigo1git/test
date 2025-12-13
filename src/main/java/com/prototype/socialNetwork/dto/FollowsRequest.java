package com.prototype.socialNetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowsRequest {

    private Integer followerId;
    private Integer followedId;
}
