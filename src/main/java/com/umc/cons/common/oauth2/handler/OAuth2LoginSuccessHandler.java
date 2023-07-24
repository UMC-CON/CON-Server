package com.umc.cons.common.oauth2.handler;

import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.common.oauth2.CustomOAuth2User;
import com.umc.cons.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if (oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

                jwtService.sendAccessToken(response, accessToken);

                response.sendRedirect("/oauth2/sign-up");

            } else if (oAuth2User.getRole() == Role.USER) {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken(oAuth2User.getEmail());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
