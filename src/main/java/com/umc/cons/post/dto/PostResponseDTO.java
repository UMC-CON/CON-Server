package com.umc.cons.post.dto;

import com.umc.cons.post.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDTO {
    private String title;
    private LocalDate createdAt;
    private double score;
    private String imageUrl;
    private String content;

    public static PostResponseDTO convertToResponseDTO(Post post){
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setTitle(post.getTitle());
        responseDTO.setScore(post.getScore());
        responseDTO.setCreatedAt(post.getCreatedAt().toLocalDate());
        responseDTO.setImageUrl(post.getImageUrl());
        responseDTO.setContent(post.getContent());

        return responseDTO;
    }
}