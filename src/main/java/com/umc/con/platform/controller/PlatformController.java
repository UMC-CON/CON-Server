package com.umc.con.platform.controller;

import com.umc.con.platform.service.PlatformService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/contents/platform")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService){
        this.platformService = platformService;
    }

}
