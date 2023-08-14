package com.umc.cons.follow.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    //A(follower) follow B(following)
    @PostMapping("/{followerId}/{followingId}")
    public BaseResponse<Follow> createFollow(@PathVariable Long followerId, @PathVariable Long followingId){
        return new BaseResponse<>(followService.createFollow(followerId,followingId));
    }
}
