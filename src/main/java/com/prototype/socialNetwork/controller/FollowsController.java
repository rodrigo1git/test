package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.FollowsRequest;
import com.prototype.socialNetwork.dto.FollowsResponse;
import com.prototype.socialNetwork.entity.Follows;
import com.prototype.socialNetwork.service.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")

public class FollowsController {

    private FollowsService followsService;

    @Autowired
    public FollowsController(FollowsService followsService){
        this.followsService = followsService;
    }

    @GetMapping
    public List<Follows> getFollowers(){
        return followsService.getFollowers();
    }

    @PostMapping
    public ResponseEntity<FollowsResponse> insertFollows(@RequestBody FollowsRequest followsRequest) {
        // 1. Insertar
        Follows newFollow = followsService.insertFollows(
                followsRequest.getFollowerId(),
                followsRequest.getFollowedId()
        );
        // 2. Mapear a DTO (Manual para evitar bucles)
        FollowsResponse response = new FollowsResponse();
        response.setFollowerId(newFollow.getFollower().getId());
        response.setFollowedId(newFollow.getFollowed().getId());
        response.setSince(newFollow.getSince());

        // Opcional: Si quieres devolver nombres, ten en cuenta que getReferenceById
        // no trae el nombre real a menos que Hibernate haga un SELECT extra.
        // Si solo te importan los IDs y la fecha, con lo de arriba basta.

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
