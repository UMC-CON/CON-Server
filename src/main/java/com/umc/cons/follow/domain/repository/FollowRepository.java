package com.umc.cons.follow.domain.repository;
import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.follow.domain.entity.Follow;
import com.umc.cons.follow.domain.entity.FollowId;
import com.umc.cons.member.domain.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    /**
     * 특정 회원이 팔로잉하는 팔로우 목록 조회
     */
    Follow findFollowByFollowId(FollowId followId);
    /**
     * 팔로우 삭제
     */
    void deleteFollowByFollowId(FollowId followId);

    @Query("SELECT f.followId.follower FROM Follow f WHERE f.followId.following = :loginmember")
    List<Member> findFollowersOfLoginMember(@Param("loginmember") Member loginmember);

    @Query("SELECT f.followId.following FROM Follow f WHERE f.followId.follower = :loginmember")
    List<Member> findFollowingsOfLoginMember(@Param("loginmember") Member loginmember);
}