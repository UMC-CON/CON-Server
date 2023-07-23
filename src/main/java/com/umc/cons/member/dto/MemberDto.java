package com.umc.cons.member.dto;

import com.umc.cons.member.domain.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberDto {
    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotEmpty
    @Pattern(message = "최소 한개 이상의 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
    private String password;

    @NotEmpty
    @Pattern(message = "최소 한개 이상의 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
    private String checkPassword;

    @NotEmpty
    private String name;

    public static Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .build();
    }

}