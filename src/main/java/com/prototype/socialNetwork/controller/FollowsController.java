package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.FollowsRequestDTO;
import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.service.FollowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FollowsController {

    private final FollowsService followsService;


    @GetMapping
    public List<FollowsResponseDTO> getFollowers(){
        return followsService.getFollowers();
    }

    @PostMapping
    public ResponseEntity<FollowsResponseDTO> insertFollows(@RequestBody FollowsRequestDTO followsRequest) {
        FollowsResponseDTO response = followsService.insertFollows(followsRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //seguidores
    @GetMapping("/{id}/followers")
    public List<FollowsResponseDTO> getFollowersByUserId(@PathVariable Integer id){
        // Si busco por "followedId" (seguido), obtengo quiénes me siguen a mí
        return followsService.findByFollowedId(id);
    }

    //seguidos
    @GetMapping("/{id}/following")
    public List<FollowsResponseDTO> getFollowingByUserId(@PathVariable Integer id){
        // Si busco por "followerId" (seguidor), obtengo a quiénes sigo yo
        return followsService.findByFollowerId(id);
    }
}