package io.kimmking.dubbo.demo.consumer.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

public class RedisDCounter {

    private RedisTemplate redisTemplate;

    private static final RedisScript<Boolean> decreaseScript = new DefaultRedisScript<>("if (tonumber(redis.call('GET', KEYS[1])) < tonumber(ARGV[1]))) then return false; else redis.call('DECRBY', KEYS[1], ARGV[1]); return true;end;",
            Boolean.class);

    public RedisDCounter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static RedisDCounter of(RedisTemplate redisTemplate, String key, long amount) {
        redisTemplate.opsForValue().set(key, amount);
        return new RedisDCounter(redisTemplate);
    }

    public Boolean decrease(String key, int amount) {
        return (Boolean) redisTemplate.execute(decreaseScript, Collections.singletonList(key), String.valueOf(amount));
    }
}
