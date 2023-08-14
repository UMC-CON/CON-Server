package com.umc.cons.follow.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.follow.service.FollowService;
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
    @PostMapping("/save/{followerId}/{followingId}")
    public BaseResponse<Follow> createFollow(@PathVariable Long followerId, @PathVariable Long followingId){
        return new BaseResponse<>(followService.createFollow(followerId,followingId));
    }

    @GetMapping("/get/following_list/{userId}")
    public BaseResponse<List<FollowId>> getFollowings(@PathVariable Long userId){
        return new BaseResponse<>(followService.findFollowingList(userId));
    }
    @GetMapping("/get/follower_list/{userId}")
    public BaseResponse<List<FollowId>> getFollowers(@PathVariable Long userId){
        return new BaseResponse<>(followService.findFollowerList(userId));
    }


}
