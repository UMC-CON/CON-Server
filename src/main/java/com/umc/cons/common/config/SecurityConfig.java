package com.umc.cons.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.cons.common.jwt.filter.JwtAuthenticationProcessingFilter;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.common.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.umc.cons.common.login.handler.LoginFailureHandler;
import com.umc.cons.common.login.handler.LoginSuccessHandler;
import com.umc.cons.common.login.service.LoginService;
import com.umc.cons.common.logout.SimpleUrlLogoutSuccessHandler;
import com.umc.cons.common.oauth2.handler.OAuth2LoginFailureHandler;
import com.umc.cons.common.oauth2.handler.OAuth2LoginSuccessHandler;
import com.umc.cons.common.oauth2.service.CustomOAuth2UserService;
import com.umc.cons.common.refreshtoken.RefreshTokenRepository;
import com.umc.cons.member.domain.entity.Role;
import com.umc.cons.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final SimpleUrlLogoutSuccessHandler logoutSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Value("${jwt.strength}")
    private int strength;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable() // form 로그인 사용 X
                .httpBasic().disable() // Http basicr Auth 기반의 로그인 인증창. 사용 X
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()

                // 세션을 사용하지 않으므로 STATELESS로 설정
                // STATELESS : 스프링시큐리티가 생성하지도 않고 기존것을 사용하지도 않음 -> 토큰 방식을쓸때 사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // URL 별 권한 관리 옵션
                .authorizeRequests()

                // 기본페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "favicon.ioc").permitAll()
                .antMatchers("/members/sign-up").permitAll() // 회원가입 접근 가능
                .antMatchers("/members/oauth2/sign-up").hasRole(Role.GUEST.toString())
                .anyRequest().hasRole(Role.USER.toString())
                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("Authorization", "Authorization-Refresh")

                .and()

                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);

        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, refreshTokenRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordALoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordALoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordALoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordALoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordALoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, memberRepository, refreshTokenRepository);
    }
}
