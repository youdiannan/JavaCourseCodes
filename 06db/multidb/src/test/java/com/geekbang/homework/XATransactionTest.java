package com.geekbang.homework;

import com.geekbang.Application;
import com.geekbang.mapper.OrderMapper;
import com.geekbang.po.OrderPO;
import com.geekbang.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class XATransactionTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    private Long orderId;

    @Before
    public void initData() {
        orderId = 724636540713566208L;
        OrderPO orderPO = orderMapper.selectById(orderId);
        if (orderPO == null) {
            orderPO = createOrderPO();
            orderPO.setId(orderId);
            orderMapper.insertWithId(orderPO);
        }
    }

    private OrderPO createOrderPO() {
        long time = System.currentTimeMillis();
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderTime(time);
        orderPO.setPayTime(time);
        orderPO.setDiscountAmount(new BigDecimal("10.00"));
        orderPO.setBuyer((long)(new Random().nextInt(2)));
        orderPO.setActualPrice(new BigDecimal("10.00"));
        orderPO.setCreateTime(time);
        orderPO.setUpdateTime(time);
        return orderPO;
    }

    @Test
    public void transferBuyerTransactionTest() {
        // 分布式事务成功
        OrderPO originOrder = orderMapper.selectById(orderId);
        orderService.transferBuyer(orderId, false);
        // buyer + 1
        OrderPO transferredOrder = orderMapper.selectById(orderId);
        Assert.assertEquals(originOrder.getBuyer() + 1, (long)transferredOrder.getBuyer());

        // 分布式事务失败
        originOrder = orderMapper.selectById(orderId);
        try {
            orderService.transferBuyer(orderId, true);
        } catch (Exception e) {

        }
        // buyer不变
        transferredOrder = orderMapper.selectById(orderId);
        Assert.assertEquals(originOrder, transferredOrder);
    }
}
