package com.umc.cons.follow.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@Data
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private Long followerID;
    @Column(name = "following_id")
    private Long followingID;
}
