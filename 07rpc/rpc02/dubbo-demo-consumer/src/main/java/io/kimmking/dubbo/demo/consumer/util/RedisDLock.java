package io.kimmking.dubbo.demo.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

@Component
public class RedisDLock {

    private static final long DEFAULT_TIMEOUT = 5000;

    private static final RedisScript<Boolean> unlockScript = new DefaultRedisScript<>("local v = redis.call('GET', KEYS[1])\n" +
            "\n" +
            "if (v == nil) then\n" +
            "    return nil\n" +
            "end\n" +
            "if (v == ARGV[1]) then \n" +
            "    redis.call('DEL', KEYS[1])\n" +
            "    return true\n" +
            "else \n" +
            "    return false\n" +
            "end",
            Boolean.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Boolean lock(String key) {
        return redisTemplate.opsForValue()
                .setIfAbsent(key, String.valueOf(Thread.currentThread().getId()), Duration.ofMillis(DEFAULT_TIMEOUT));
    }

    public Boolean unlock(String key) {
        return redisTemplate.execute(unlockScript, Collections.singletonList(key), String.valueOf(Thread.currentThread().getId()));
    }
}
