package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.dto.PostCommentRequestDTO;
import com.prototype.socialNetwork.dto.PostCommentResponseDTO;
import java.util.List;

public interface PostCommentService {
    List<PostCommentResponseDTO> getAllComments();
    List<PostCommentResponseDTO> getCommentsByPostId(Integer postId);
    List<PostCommentResponseDTO> getCommentsByProfileId(Integer profileId);
    PostCommentResponseDTO insertComment(PostCommentRequestDTO request, String imageUrl);
}