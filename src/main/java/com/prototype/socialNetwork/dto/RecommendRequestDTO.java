package com.prototype.socialNetwork.dto;

import lombok.Data;

@Data
public class RecommendRequestDTO {
    private float[] vector;
    private Integer id;
}
