package com.umc.cons.content.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.content.dto.ContentRequestDto;
import com.umc.cons.content.dto.ContentResponseDto;
import com.umc.cons.content.dto.MultipleContentRequestDto;
import com.umc.cons.content.dto.MultipleContentResponseDto;
import com.umc.cons.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/app/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping("")
    public BaseResponse<MultipleContentResponseDto> createContents(@RequestBody MultipleContentRequestDto multipleContentRequestDto){
        MultipleContentResponseDto responseDto = contentService.createContents(multipleContentRequestDto);
        return new BaseResponse<>(responseDto);
    }
    @GetMapping("/{id}")
    public BaseResponse<ContentResponseDto> getContent(@PathVariable Long id){
        return new BaseResponse<>(contentService.getContent(id));
    }

}
