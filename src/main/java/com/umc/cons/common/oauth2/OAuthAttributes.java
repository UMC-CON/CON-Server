package com.umc.cons.common.oauth2;

import com.umc.cons.common.oauth2.userinfo.NaverOAuth2UserInfo;
import com.umc.cons.common.oauth2.userinfo.OAuth2UserInfo;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.entity.Role;
import com.umc.cons.member.domain.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 값
    private OAuth2UserInfo oAuth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /**
     * Social 타입에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * userNameAttributeName : OAuth2 로그인 시 키(PK)가 되는 값
     * attribute : OAuth 서비스의 유저 정보들
     */
    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
            return ofNaver(userNameAttributeName, attributes);

    }

    // 소셜 타입 별로 나눠서 빌더로 OAuthAttributes 빌드 시 유저 정보 추상 클래스인 OAuth2UserInfo 필드에 각 소셜 타입의 OAuth2UserInfo 를 생성하여 빌드
    // 각각 소셜 로그인 API에서 제공하는 회원 식별값(Id), attributes, nameAttributeKey를 저장 후 build
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }


    // UserEntity로 변경
    public Member toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail()) // 받아온 이메일 값으로 저장
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }
}

