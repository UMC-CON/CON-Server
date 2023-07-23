package com.umc.cons.platform.service;

import com.umc.cons.platform.domain.entity.Platform;
import com.umc.cons.platform.domain.repository.PlatformRepository;
import com.umc.cons.platform.dto.PlatformRequestDto;
import com.umc.cons.platform.dto.PlatformResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformService(PlatformRepository platformRepository){
        this.platformRepository = platformRepository;
    }

    public List<PlatformResponseDto> getPlatforms(){

        List<Platform> platforms = platformRepository.findAll();
        List<PlatformResponseDto> platformResponseDtos = new LinkedList<>();
        for(Platform platform : platforms){
            PlatformResponseDto platformResponseDto = new PlatformResponseDto();
            platformResponseDto.setId(platform.getId());
            platformResponseDto.setName(platform.getName());
            platformResponseDtos.add(platformResponseDto);
        }
        return platformResponseDtos;
    }

    public PlatformResponseDto createPlatform(PlatformRequestDto requestDto){
        Platform platform = new Platform();
        platform.setName(requestDto.getName());
        Platform savedPlatform = platformRepository.save(platform);
        PlatformResponseDto responseDto = new PlatformResponseDto();
        responseDto.setId(savedPlatform.getId());
        responseDto.setName(savedPlatform.getName());
        return responseDto;
    }

}
