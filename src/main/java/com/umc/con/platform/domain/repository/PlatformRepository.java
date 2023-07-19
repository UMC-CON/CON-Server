package com.umc.con.platform.domain.repository;


import com.umc.con.platform.domain.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Integer> {
}
