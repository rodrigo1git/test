package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostCommentRequestDTO;
import com.prototype.socialNetwork.dto.PostCommentResponseDTO;
import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostComment;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.PostCommentRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentServiceJpa implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    // --- MAPPER (Entidad -> DTO) ---
    private PostCommentResponseDTO mapToDTO(PostComment comment) {
        PostCommentResponseDTO dto = new PostCommentResponseDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        dto.setImageUrl(comment.getImageUrl());
        dto.setCommentDate(comment.getCommentDate());
        dto.setPostId(comment.getPost().getPostId());
        dto.setAuthorId(comment.getProfile().getId());
        dto.setAuthorName(comment.getProfile().getPublicName());
        return dto;
    }

    // --- MÉTODOS DE LECTURA ---

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentResponseDTO> getAllComments() {
        List<PostComment> comments = postCommentRepository.findAll();
        List<PostCommentResponseDTO> dtos = new ArrayList<>();
        for (PostComment comment : comments) {
            PostCommentResponseDTO dto = mapToDTO(comment);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentResponseDTO> getCommentsByPostId(Integer postId) {
        List<PostComment> comments = postCommentRepository.findByPostId(postId);
        List<PostCommentResponseDTO> dtos = new ArrayList<>();
        for (PostComment comment : comments) {
            dtos.add(mapToDTO(comment));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentResponseDTO> getCommentsByProfileId(Integer profileId) {
        List<PostComment> comments = postCommentRepository.findByProfileId(profileId);
        List<PostCommentResponseDTO> dtos = new ArrayList<>();
        for (PostComment comment : comments) {
            dtos.add(mapToDTO(comment));
        }
        return dtos;
    }

    // --- MÉTODO DE ESCRITURA ---

    @Override
    @Transactional
    public PostCommentResponseDTO insertComment(PostCommentRequestDTO request, String imageUrl) {
        Post post = postRepository.getReferenceById(request.getPostId());
        Profile author = profileRepository.getReferenceById(request.getProfileId());
        PostComment comment = new PostComment();
        comment.setBody(request.getBody());
        comment.setImageUrl(imageUrl); // Puede ser null
        comment.setCommentDate(LocalDate.now());
        comment.setPost(post);
        comment.setProfile(author);
        PostComment savedComment = postCommentRepository.save(comment);
        return mapToDTO(savedComment);
    }

}