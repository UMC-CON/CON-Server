package com.umc.cons.content.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentResponseDto {
    private Long id;

    private String name;

    private String image;

    private String genre;
}
