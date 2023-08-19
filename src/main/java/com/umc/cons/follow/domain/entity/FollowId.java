package com.umc.cons.follow.domain.entity;

import com.umc.cons.member.domain.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class FollowId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Member follower;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id",nullable = false)
    private Member following;

}
