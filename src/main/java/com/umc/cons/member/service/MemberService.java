package com.umc.cons.member.service;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatedName(String name) {
        return memberRepository.existsByName(name);
    }

    public boolean checkPassword(String password, String checkPassword) {
        if (password == checkPassword) {
            return true;
        }
        return false;
    }

    public void registerMember(Member member) {
        memberRepository.save(member);
    }

    public void registerOAuth2Member(String email, String name) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        member.registerOAuth2User(name);
    }
}
