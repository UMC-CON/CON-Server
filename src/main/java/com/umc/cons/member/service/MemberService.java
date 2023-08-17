package com.umc.cons.member.service;

import com.umc.cons.common.jwt.exception.InvalidJwtException;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.member.dto.MemberPageResponse;
import com.umc.cons.member.dto.MemberResponse;
import com.umc.cons.member.dto.ProfileRequestDto;
import com.umc.cons.member.exception.MemberNotFoundException;
import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.domain.repository.NotificationRepository;
import com.umc.cons.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;
    private final JwtService jwtService;
    private final NotificationRepository notificationRepository;

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatedName(String name) {
        return memberRepository.existsByName(name);
    }

    public boolean checkPassword(String password, String checkPassword) {
        return password.equals(checkPassword);
    }

    public void registerMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void registerOAuth2Member(Member member, String name) {
        member.registerOAuth2User(name);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member getLoginMember() {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(InvalidJwtException::new);
        String email = jwtService.extractEmail(accessToken).orElseThrow(InvalidJwtException::new);

        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public MemberPageResponse findAllByName(String name, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByNameContainingIgnoreCase(name, pageable);

        return getMemberPageResponse(members, pageable);
    }

    @Transactional(readOnly = true)
    public MemberPageResponse getMemberPageResponse(Page<Member> members, Pageable pageable) {

        List<MemberResponse> memberResponse = members
                .getContent()
                .stream()
                .map(MemberResponse::of)
                .collect(Collectors.toList());

        return MemberPageResponse.builder()
                .totalPage(members.getTotalPages())
                .currentPage(pageable.getPageNumber())
                .memberResponses(memberResponse)
                .build();
    }

    @Transactional
    public void updatePassword(Member member, PasswordEncoder passwordEncoder, String password) {
        member.updatePassword(passwordEncoder.encode(password));

        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Member member) {
        member.deleteMember();
        notificationRepository.updateDeleteAllByMember(member);

        memberRepository.save(member);
    }

    @Transactional
    public void updateProfile(Member member, ProfileRequestDto profileRequest) {
        member.updateProfile(profileRequest);

        memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
    }
}
