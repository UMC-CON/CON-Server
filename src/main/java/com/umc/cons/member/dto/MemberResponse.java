package com.umc.cons.member.dto;

import com.umc.cons.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String name;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .name(member.getName())
                .build();
    }
}
