package io.kimmking.dubbo.demo.consumer.service;

import io.kimmking.dubbo.demo.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService, MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void pubOrder(Order order) {
        redisTemplate.convertAndSend("pub:test", order);
        System.out.println("message published");
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("message received: " + new String(message.getBody()));
    }
}
