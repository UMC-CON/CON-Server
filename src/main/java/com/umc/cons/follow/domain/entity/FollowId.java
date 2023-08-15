package com.umc.cons.follow.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class FollowId implements Serializable {
    //for testing
    @Column(name = "follower_id", nullable = false)
    private Long followerId;
    @Column(name = "following_id", nullable = false)
    private Long followingId;

    // 실제 mapping관계 반영

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    //follower.getId()로 메서드 변경 필요
    private Member follower;
    //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;
    */

}
