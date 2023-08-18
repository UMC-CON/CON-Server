package com.umc.cons.follow.service;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.follow.domain.repository.FollowRepository;
import com.umc.cons.follow.exception.FollowException;
import com.umc.cons.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        if(followRepository.findFollowByFollowId(follow.getFollowId())!= null){
            throw new FollowException("이미 팔로우 하고 있습니다.");
        }
        if(follow.getFollowId().getFollower().equals(follow.getFollowId().getFollowing())){
            throw new FollowException("자기 자신을 팔로우 할 수 없습니다.");
        }
    }
    //특정 Follow 조회
    public Follow findOne(FollowId followId) {
        return followRepository.findFollowByFollowId(followId);
    }

    //Follow 목록 삭제
    public void deleteOne(FollowId followId){
        validateDeleteFollow(followId);
        followRepository.deleteFollowByFollowId(followId);
    }
    public void validateDeleteFollow(FollowId followId){
        if(followRepository.findFollowByFollowId(followId)== null){
            throw new FollowException("현재 팔로우 하고 있는 않은 관계를 삭제 하려고 하였습니다.");
        }
    }
    /**
     *
     * Follow 생성 및 데이터베이스에 저장
     */
    @Transactional
    public Follow createFollow(Member follower, Member following){
        FollowId followId = new FollowId();
        followId.setFollower(follower);
        followId.setFollowing(following);
        Follow follow = new Follow();
        follow.setFollowId(followId);
        this.join(follow);
        follower.follow(following);
        return follow;
    }

    /**
     * 특정 회원이 팔로우한 목록
     */
    public List<Member> findFollowerList(@LoginMember Member member){
        return member.getFollowers();
    }
    /**
     * 특정 회원을 팔로우하는 목록
     */
    public List<Member> findFollowingList(@LoginMember Member member){
        return member.getFollowings();
    }
    @Transactional
    public FollowId deleteFollow(Member follower, Member following){
        FollowId followId = new FollowId();
        followId.setFollower(follower);
        followId.setFollowing(following);
        this.deleteOne(followId);
        follower.unfollow(following);
        return followId;
    }
}
