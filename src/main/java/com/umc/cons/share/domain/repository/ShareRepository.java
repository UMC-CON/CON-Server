package com.umc.cons.share.domain.repository;

import com.umc.cons.share.domain.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Page<Share> findAllBy(Pageable pageable);
}