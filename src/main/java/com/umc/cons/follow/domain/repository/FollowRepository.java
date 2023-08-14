package com.umc.cons.follow.domain.repository;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    /**
     * 특정 회원을 팔로우하는 팔로우 목록 조회
     */
    List<Follow> findFollowsByFollowIdFollowerId(Long followerId);

    /**
     * 특정 회원이 팔로잉하는 팔로우 목록 조회
     */
    List<Follow> findFollowsByFollowIdFollowingId(Long followingId);

    /**
     * 특정 회원이 팔로잉하는 팔로우 목록 조회
     */
    Follow findFollowByFollowId(FollowId followId);

    /**
     * 팔로우 삭제
     */
    void deleteByFollowId(FollowId followId);


}
