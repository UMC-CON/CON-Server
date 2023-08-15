package com.umc.cons.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPageResponse {

    private int totalPage;
    private int currentPage;
    private List<MemberResponse> memberResponses = new ArrayList<>();

    @Builder
    public MemberPageResponse(int totalPage, int currentPage, List<MemberResponse> memberResponses) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.memberResponses = memberResponses;
    }
}
