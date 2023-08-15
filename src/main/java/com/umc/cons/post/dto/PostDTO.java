package com.umc.cons.post.dto;

import com.umc.cons.common.util.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;


@Getter
@Setter
@NoArgsConstructor
public class PostDTO extends BaseTimeEntity {
    private Long contentId;
    private String title;
    private String imageUrl;
    private MultipartFile imageFile;
    private String content;

    @Min(value = 0, message = "점수는 0보다 크거나 같아야 합니다.")
    @Max(value = 5, message = "점수는 5보다 작거나 같아야 합니다.")
    private double score;
}