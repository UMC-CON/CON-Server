package com.umc.cons.common.blacklist;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "blackList")
public class BlackList {

    @Id
    private String accessToken;

    @TimeToLive
    private Long expiration;

    public BlackList(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
