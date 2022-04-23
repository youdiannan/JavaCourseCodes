package com.geekbang.homework;

import com.geekbang.Application;
import com.geekbang.mapper.OrderMapper;
import com.geekbang.po.OrderPO;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ShardingTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testInsert() {
        long time = System.currentTimeMillis();
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderTime(time);
        orderPO.setPayTime(time);
        orderPO.setDiscountAmount(new BigDecimal("10.00"));
        orderPO.setBuyer((long)(new Random().nextInt(2)));
        orderPO.setActualPrice(new BigDecimal("10.00"));
        orderPO.setCreateTime(time);
        orderPO.setUpdateTime(time);

        int count = orderMapper.insert(orderPO);
        System.out.println(count + " inserted.");
    }

    @Test
    public void testSelect() {
        List<OrderPO> orderPOS = orderMapper.selectAll();
        System.out.println(new Gson().toJson(orderPOS));
    }

    @Test
    @Transactional
    public void testDelete() {
        long orderId = 724636540713566208L;
        System.out.println("===================");
        System.out.println(orderMapper.selectById(orderId));
        orderMapper.delete(724636540713566208L);
        System.out.println(orderMapper.selectById(orderId));
        System.out.println("===================");
    }

    @Test
    @Transactional
    public void testUpdate() {
        long orderId = 724636540713566208L;
        System.out.println("===================");
        System.out.println(orderMapper.selectById(orderId));
        orderMapper.cancel(orderId);
        System.out.println(orderMapper.selectById(orderId));
        System.out.println("===================");
    }
}
