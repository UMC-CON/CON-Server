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
                .antMatchers("/sign-up").permitAll() // 회원가입 접근 가능
                .antMatchers("/oauth2/sign-up").hasRole(Role.GUEST.toString()) // GUEST 회원만 접근 가능한 OAuth2 유저의 회원가입
                .anyRequest().authenticated() // 위의 경로 외에는 모두 인증된 사용자만 접근 가능
                .and()

                .logout() // 로그아웃 기능 작동함
                .logoutUrl("/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동페이지
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("Authorization", "Authorization-Refresh") // 로그아웃 후 쿠키 삭제

                .and()
                // 소셜 로그인 설정
                .oauth2Login() // OAuth2LoginConfigurer을 반환하여 OAuth2 로그인에 관한 다양한 기능을 사용
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속 하기 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService); // OAuth2 로그인의 로직을 담당하는 Service를 설정


        // LogoutFilter 이후에 로그인 필터 동작 수행
        // LogoutFilter -> jwtAuthenticationProcessingFilter -> customJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }

    /**
     * AutenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정
     * FormLogin과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
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

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthentcationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler tjfwjd
     */
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
