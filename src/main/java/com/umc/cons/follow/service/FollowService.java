package com.umc.cons.follow.service;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.follow.domain.repository.FollowRepository;
import com.umc.cons.follow.exception.AlreadyExistFollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    //Following 저장 로직
    @Transactional
    public FollowId join(Follow follow){
        //중복 회원 검증
        validateDuplicateFollow(follow);
        //저장
        followRepository.save(follow);
        return follow.getFollowId();
    }
    //중복 follow 요청 처리
    public void validateDuplicateFollow(Follow follow){
        if(followRepository.findFollowByFollowID(follow.getFollowId())==null){
            throw new AlreadyExistFollowException("이미 팔로우 하고 있습니다.");
        }
    }
    //특정 Follow 조회
    public Follow findOne(FollowId followId) {
        return followRepository.findFollowByFollowID(followId);
    }

    //Follow 목록 삭제
    public void deleteOne(FollowId followId){
        followRepository.deleteByFollowId(followId);
    }

    @Transactional
    public Follow createFollow(Long followerId, Long followingId){
        FollowId followId = new FollowId();
        followId.setFollowerID(followerId);
        followId.setFollowingID(followingId);
        Follow follow = new Follow();
        follow.setFollowId(followId);
        this.join(follow);
        return follow;
    }

}
