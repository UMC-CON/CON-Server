package com.umc.cons.post.domain.repository;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByMember(Member member, Pageable pageable);
}