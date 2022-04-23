package com.geekbang.service;

import com.geekbang.mapper.OrderMapper;
import com.geekbang.po.OrderPO;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 将一个用户的订单转移到另一个用户
     * 方法中简单处理为用户id+1,使delete和insert命中不同的库
     * @param orderId
     */
    @Transactional
    @ShardingSphereTransactionType(TransactionType.XA)
    public void transferBuyer(long orderId, boolean needError) {
        OrderPO orderPO = orderMapper.selectById(orderId);
        orderPO.setBuyer(orderPO.getBuyer() + 1);

        orderMapper.delete(orderId);
        if (needError) {
            throw new RuntimeException("error");
        }
        orderMapper.insertWithId(orderPO);
    }
}
