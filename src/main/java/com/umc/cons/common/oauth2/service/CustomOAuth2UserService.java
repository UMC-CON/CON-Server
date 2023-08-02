package com.umc.cons.common.oauth2.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.umc.cons.common.oauth2.CustomOAuth2User;
import com.umc.cons.common.oauth2.OAuthAttributes;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.entity.SocialType;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.member.exception.MemberDuplicatedException;
import com.umc.cons.member.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final String NAVER = "naver";
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		OAuthAttributes extractAttributes = OAuthAttributes.of(userNameAttributeName, attributes);

		Member createdMember = getMember(extractAttributes, socialType);

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().getKey())),
			attributes,
			extractAttributes.getNameAttributeKey(),
			createdMember.getEmail(),
			createdMember.getRole()
		);
	}

	private Member getMember(OAuthAttributes attributes, SocialType socialType) {
		Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType,
			attributes.getOAuth2UserInfo().getId()).orElse(null);

		if (findMember == null) {
			return saveUser(attributes, socialType);
		}

		if (findMember.isDeleted()) {
			throw new MemberNotFoundException("삭제된 멤버입니다");
		}

		return findMember;
	}

	private Member saveUser(OAuthAttributes attributes, SocialType socialType) {
		Member createdMember = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());

		boolean emailExists = memberRepository.existsByEmail(createdMember.getEmail());
		if (emailExists) {
			throw new MemberDuplicatedException();
		}
		return memberRepository.save(createdMember);
	}

	private SocialType getSocialType(String registrationId) {
		if (NAVER.equals(registrationId)) {
			return SocialType.NAVER;
		}
		return SocialType.OUR;
	}
}
