package com.umc.cons.follow.controller;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.follow.service.FollowService;
import com.umc.cons.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    /**
     *  follow 저장 로직
     *  A(follower) follow B(following)
     */
    @PostMapping("")
    public BaseResponse<Follow> createFollow(@LoginMember Member loginmember, @RequestBody Member following){
        return new BaseResponse<>(followService.createFollow(loginmember,following));
    }

    @GetMapping("/following_list")
    public BaseResponse<List<Member>> getFollowings(@LoginMember Member loginmember){
        return new BaseResponse<>(followService.findFollowingList(loginmember));
    }
    @GetMapping("/follower_list")
    public BaseResponse<List<Member>> getFollowers(@LoginMember Member loginmember){
        return new BaseResponse<>(followService.findFollowerList(loginmember));
    }
    @DeleteMapping("")
    public BaseResponse<FollowId> deleteFollow(@LoginMember Member loginmember, @RequestBody Member following){
        return new BaseResponse<>(followService.deleteFollow(loginmember,following));

    }


}
