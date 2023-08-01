package com.umc.cons.record.domain.repository;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.record.domain.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByPost(Post post);
}