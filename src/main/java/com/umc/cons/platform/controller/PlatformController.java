package com.umc.cons.platform.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.platform.dto.PlatformRequestDto;
import com.umc.cons.platform.dto.PlatformResponseDto;
import com.umc.cons.platform.service.PlatformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/contents/platform")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService){
        this.platformService = platformService;
    }

    @PostMapping("")
    public BaseResponse<PlatformResponseDto> createPlatform(@RequestBody PlatformRequestDto platformRequestDto){
        return new BaseResponse<>(platformService.createPlatform(platformRequestDto));
    }

    @GetMapping("")
    public BaseResponse<List<PlatformResponseDto>> getPlatforms(){
        return new BaseResponse<>(platformService.getPlatforms());
    }
}
