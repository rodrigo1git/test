package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.FollowsRequestDTO;
import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.entity.FollowsId;
import com.prototype.socialNetwork.repository.FollowsRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // Cambio: Inyección por constructor automática
public class FollowsServiceJpa implements FollowsService {

    // Cambio: 'final' para que Lombok genere el constructor
    private final FollowsRepository followsRepository;
    private final ProfileRepository profileRepository;

    private FollowsResponseDTO followsResponseDTOMapping(Follows follows){
        FollowsResponseDTO response = new FollowsResponseDTO();
        response.setFollowedId(follows.getId().getFollowedId());
        response.setFollowerId(follows.getId().getFollowerId());
        response.setSince(follows.getSince());
        response.setFollowedName(profileRepository.getReferenceById(response.getFollowedId()).getPublicName());
        response.setFollowerName(profileRepository.getReferenceById(response.getFollowerId()).getPublicName());
        return response;
    }

    @Override
    public List<FollowsResponseDTO> getFollowers() {
        List<Follows> follows = followsRepository.findAll();
        List<FollowsResponseDTO> dtos = new ArrayList<>();
        for(Follows f: follows){
            dtos.add(followsResponseDTOMapping(f));
        }
        return dtos;
    }

    @Transactional // Ahora usa org.springframework.transaction.annotation.Transactional
    @Override
    public FollowsResponseDTO insertFollows(FollowsRequestDTO followsRequestDTO) {
        Integer followerId = followsRequestDTO.getFollowerId();
        Integer followedId = followsRequestDTO.getFollowedId();
        FollowsId followsId = new FollowsId(followerId, followedId);
        Follows follow = new Follows(followsId, profileRepository.getReferenceById(followerId), profileRepository.getReferenceById(followedId), LocalDateTime.now());
        Follows savedFollow = followsRepository.save(follow);
        return followsResponseDTOMapping(savedFollow);
    }

    @Override
    public List<FollowsResponseDTO> findByFollowerId(Integer id) {
        List<Follows> follows = followsRepository.findByFollowerId(id);
        List<FollowsResponseDTO> dtos = new ArrayList<>();
        for(Follows f: follows){
            dtos.add(followsResponseDTOMapping(f));
        }
        return dtos;
    }

    @Override
    public List<FollowsResponseDTO> findByFollowedId(Integer id) {
        List<Follows> follows = followsRepository.findByFollowedId(id);
        List<FollowsResponseDTO> dtos = new ArrayList<>();
        for(Follows f: follows){
            dtos.add(followsResponseDTOMapping(f));
        }
        return dtos;
    }


}