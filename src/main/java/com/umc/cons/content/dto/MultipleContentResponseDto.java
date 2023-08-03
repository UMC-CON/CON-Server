package com.umc.cons.content.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MultipleContentResponseDto {
    private List<ContentResponseDto> contents;

    public MultipleContentResponseDto(List<ContentResponseDto> contentResponseDtos) {
        this.contents = contentResponseDtos;
    }
}
