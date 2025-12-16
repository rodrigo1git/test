package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.FollowsRequestDTO;
import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.entity.Follows;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowsService {

    public List<FollowsResponseDTO> getFollowers();

    public FollowsResponseDTO insertFollows(FollowsRequestDTO followsRequestDTO);


    public List<FollowsResponseDTO> findByFollowerId(Integer id);

    public List<FollowsResponseDTO> findByFollowedId(Integer id);

}
