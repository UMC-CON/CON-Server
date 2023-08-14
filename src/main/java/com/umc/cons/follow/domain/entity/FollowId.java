package com.umc.cons.follow.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private Long followerID;
    @Column(name = "following_id")
    private Long followingID;
}
