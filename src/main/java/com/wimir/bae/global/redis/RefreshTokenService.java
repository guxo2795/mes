package com.wimir.bae.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String userCode, String refreshToken) {
        redisTemplate.opsForValue().set(userCode, refreshToken);
        redisTemplate.expire(userCode, 30, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userCode) {
        return redisTemplate.opsForValue().get(userCode);
    }

    public void deleteRefreshToken(String userCode) {
        redisTemplate.delete(userCode);
    }
}
