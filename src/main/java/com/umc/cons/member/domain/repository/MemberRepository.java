package com.umc.cons.member.domain.repository;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.entity.SocialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmail(String email);

    public boolean existsByName(String name);

    public Optional<Member> findByEmail(String email);

    public Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query(value = "SELECT m FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT(:name, '%')) AND m.isDeleted = false",
            countQuery = "SELECT COUNT(*) FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT(:name, '%')) AND m.isDeleted = false")
    public Page<Member> findAllByNameContainingIgnoreCase(String name, Pageable pageable);


}
