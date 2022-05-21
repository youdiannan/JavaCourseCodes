package io.kimmking.dubbo.demo.consumer.service;

import io.kimmking.dubbo.demo.api.Order;

public interface OrderService {

    void pubOrder(Order order);
}
