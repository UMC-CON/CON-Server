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
//    @PostMapping("/save")
//    public BaseResponse<ContentResponseDto> createContents(@RequestBody MultipleContentRequestDto multipleContentRequestDto){
//
//        return new BaseResponse<>(contentService.createContents(multipleContentRequestDto));
//    }
    @PostMapping("/save")
    public BaseResponse<MultipleContentResponseDto> createContents(@RequestBody MultipleContentRequestDto multipleContentRequestDto){
        MultipleContentResponseDto responseDto = contentService.createContents(multipleContentRequestDto);
        return new BaseResponse<>(responseDto);
    }
//    @PutMapping("/save")
//    public BaseResponse<ContentResponseDto> updateContent(@RequestBody ContentRequestDto contentRequestDto){
//        return new BaseResponse<>(contentService.createContent(contentRequestDto));
//    }
}
