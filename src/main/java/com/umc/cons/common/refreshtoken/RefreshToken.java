package com.umc.cons.common.refreshtoken;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public void setRefreshTokenAndTimeToLive(String refreshToken, Long Expiration) {
        this.refreshToken = refreshToken;
        this.expiration = Expiration;
    }

    public RefreshToken(String email) {
        this.email = email;
    }
}
