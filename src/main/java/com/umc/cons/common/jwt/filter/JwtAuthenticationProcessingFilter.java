package com.umc.cons.common.jwt.filter;

import com.umc.cons.common.jwt.exception.InvalidJwtException;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.common.jwt.util.PasswordUtil;
import com.umc.cons.common.refreshtoken.RefreshToken;
import com.umc.cons.common.refreshtoken.RefreshTokenRepository;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isRefreshTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            String email = jwtService.extractEmail(refreshToken).orElseThrow(InvalidJwtException::new);
            checkRefreshTokenAndReIssueAccessToken(response, email);
            return;
        }

        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

    }


    private String reIssueRefreshToken(RefreshToken refreshToken, String email) {
        String reIssuedRefreshToken = jwtService.createRefreshToken(email);
        refreshToken.setRefreshTokenAndTimeToLive(reIssuedRefreshToken,
                refreshTokenExpirationPeriod/1000);
        refreshTokenRepository.save(refreshToken);
        return reIssuedRefreshToken;
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String email) {
        RefreshToken token = refreshTokenRepository.findById(email).orElseThrow();
        String reIssuedRefreshToken = reIssueRefreshToken(token, email);
        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(token.getEmail()
        ), reIssuedRefreshToken);
    }



    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isAccessTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        String password = member.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호를 임의로 설정하여 소셜 로그인 유저도 인증되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = User.builder()
                .username(member.getEmail())
                .password(password)
                .roles(member.getRole().name()) // authorities에 저장
                .build();


        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
