package com.example.task_management.common.cache;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


public interface RedisService {
    <T> void set(String key, T value, long timeout, TimeUnit unit);
    <T> T get(String key, Class<T> type);
    <T> void delete(String key);
    void deleteByPattern(String pattern);
    boolean hasKey(String key);
}
