package com.umc.cons.platform.domain.repository;

import com.umc.cons.platform.domain.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Integer> {
}
