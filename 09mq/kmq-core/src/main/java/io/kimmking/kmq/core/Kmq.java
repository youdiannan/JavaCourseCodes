package io.kimmking.kmq.core;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class Kmq {

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity];
        this.writePos = new AtomicInteger(0);
        this.consumerOffsetMap = new ConcurrentHashMap<>();
    }

    private String topic;

    private int capacity;

    private KmqMessage[] queue;

    private AtomicInteger writePos;

    private Map<String, Integer> consumerOffsetMap;

    public boolean send(KmqMessage message) {
        try {
            queue[writePos.getAndIncrement()] = message;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    public KmqMessage poll(String consumerId, int offset) {
        if (StringUtils.hasLength(consumerId)) {
            offset = consumerOffsetMap.getOrDefault(consumerId, offset);
        }
        if (offset >= capacity) {
            throw new RuntimeException("offset out of bound");
        }
        return queue[offset];
    }

    public boolean commit(String consumerId, int offset) {
        if (!StringUtils.hasLength(consumerId)) {
            return true;
        }
        consumerOffsetMap.put(consumerId, offset);
        return true;
    }
}
