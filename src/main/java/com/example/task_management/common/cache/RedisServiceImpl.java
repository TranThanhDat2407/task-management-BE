package com.example.task_management.common.cache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{
    private final StringRedisTemplate redisTemplate;

    @Override
    public <T> void set(String key, T value, long timeout, TimeUnit unit) {

    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return null;
    }

    @Override
    public <T> void delete(String key) {

    }

    @Override
    public void deleteByPattern(String pattern) {

    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }
}
