package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.dto.FollowsRequestDTO;
import com.prototype.socialNetwork.dto.FollowsResponseDTO;
import com.prototype.socialNetwork.service.FollowsService;
import lombok.RequiredArgsConstructor; // Cambio: Lombok
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // Cambio: Inyección por constructor automática
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

    @GetMapping("/followers/{id}")
    public List<FollowsResponseDTO> findByFollowerId(@PathVariable Integer id){
        return followsService.findByFollowerId(id);
    }

    @GetMapping("/followed/{id}")
    public List<FollowsResponseDTO> findByFollowedId(@PathVariable Integer id){
        return followsService.findByFollowedId(id);
    }
}