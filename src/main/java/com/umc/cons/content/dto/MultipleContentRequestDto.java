package com.umc.cons.content.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MultipleContentRequestDto {
    private List<ContentRequestDto> contents;
    public MultipleContentRequestDto(List<ContentRequestDto> contents) {
        this.contents = contents;
    }
}
