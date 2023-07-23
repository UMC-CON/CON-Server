package com.umc.cons.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.cons.platform.dto.PlatformRequestDto;
import com.umc.cons.platform.dto.PlatformResponseDto;
import com.umc.cons.platform.service.PlatformService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PlatformController.class)
class PlatformControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlatformService platformService;

    @Test
    void createPlatform() throws Exception {

        PlatformRequestDto requestDto = new PlatformRequestDto();
        requestDto.setName("test");

        PlatformResponseDto responseDto = new PlatformResponseDto();
        responseDto.setId(1L);
        responseDto.setName(requestDto.getName());

        given(platformService.createPlatform(any(PlatformRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/app/contents/platform")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.name").value(requestDto.getName()));
    }

    @Test
    void getPlatforms() throws Exception {

        PlatformResponseDto platform1 = new PlatformResponseDto();
        platform1.setId(1L);
        platform1.setName("Platform 1");

        PlatformResponseDto platform2 = new PlatformResponseDto();
        platform2.setId(2L);
        platform2.setName("Platform 2");

        List<PlatformResponseDto> platforms = Arrays.asList(platform1, platform2);

        given(platformService.getPlatforms()).willReturn(platforms);

        mockMvc.perform(get("/app/contents/platform"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result[0].id").value(1L))
                .andExpect(jsonPath("$.result[0].name").value("Platform 1"))
                .andExpect(jsonPath("$.result[1].id").value(2L))
                .andExpect(jsonPath("$.result[1].name").value("Platform 2"));
    }
}