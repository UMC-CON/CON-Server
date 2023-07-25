package com.umc.cons.member.service;

import com.umc.cons.common.jwt.exception.InvalidJwtException;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.member.dto.MemberDto;
import com.umc.cons.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;
    private final JwtService jwtService;

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

    public void registerOAuth2Member(Member member, String name) {
        member.registerOAuth2User(name);
    }

    public Member getLoginMember() {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(InvalidJwtException::new);
        String email = jwtService.extractEmail(accessToken).orElseThrow(InvalidJwtException::new);

        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

}
