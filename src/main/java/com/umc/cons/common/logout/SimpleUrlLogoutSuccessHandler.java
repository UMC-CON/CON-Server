package com.umc.cons.common.logout;


import com.umc.cons.common.jwt.exception.InvalidJwtException;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.common.refreshtoken.RefreshToken;
import com.umc.cons.common.refreshtoken.RefreshTokenRepository;
import com.umc.cons.common.refreshtoken.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class SimpleUrlLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(InvalidJwtException::new);
        String email = jwtService.extractEmail(accessToken).orElseThrow(InvalidJwtException::new);

        logout(email);
    }

    public void logout(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElseThrow(RefreshTokenNotFoundException::new);
        refreshTokenRepository.delete(refreshToken);
    }
}
