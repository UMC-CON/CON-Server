package com.umc.con.platform.service;

import com.umc.con.platform.domain.entity.Platform;
import com.umc.con.platform.domain.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformService(PlatformRepository platformRepository){
        this.platformRepository = platformRepository;
    }

    public List<Platform> getList(){
        return this.platformRepository.findAll();
    }

    public void insertPlatform(String name){
        Platform platform = new Platform();
        platform.setName(name);
        this.platformRepository.save(platform);
    }

}
