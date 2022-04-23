package com.geekbang.controller;

import com.geekbang.config.DynamicDataSource;
import com.geekbang.vo.OrderVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@RestController
public class ShadowController {

    @Resource(name = "dynamicDataSource")
    private DataSource dynamicDataSource;

    @Resource(name = "shardingShadowDataSource")
    private DataSource shardingDataSource;

    @GetMapping("/dynamicDS")
    public OrderVO dynamicDataSourceDemo(@RequestParam("id") long id, @RequestParam("shadow") boolean shadow) {
        // 拦截器
        if (shadow) {
            // 没有插入数据
            DynamicDataSource.setLookupKey(DynamicDataSource.SHADOW_DATASOURCE);
        } else {
            DynamicDataSource.setLookupKey(DynamicDataSource.DEFAULT_DATASOURCE);
        }

        return selectById(id, dynamicDataSource);
    }

    @GetMapping("/shardingDS")
    public OrderVO shardingDataSourceDemo(@RequestParam("id") long id) {
        return selectById(id, shardingDataSource);
    }

    private OrderVO selectById(long id, DataSource dataSource) {
        try {
            String selectSql = "SELECT * FROM TB_MALL_ORDER WHERE Id = ?";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            OrderVO orderVO = new OrderVO();
            if (!resultSet.next()) {
                return orderVO;
            }

            orderVO.setId(resultSet.getLong(1));
            orderVO.setOrderTime(resultSet.getLong(2));
            orderVO.setPayTime(resultSet.getLong(3));
            orderVO.setDiscountAmount(resultSet.getBigDecimal(4));
            orderVO.setActualPrice(resultSet.getBigDecimal(5));
            orderVO.setOrderStatus(resultSet.getInt(6));
            orderVO.setBuyer(resultSet.getLong(7));
            orderVO.setRemark(resultSet.getString(8));
            orderVO.setCancelStatus(resultSet.getInt(9));
            orderVO.setCancelTime(resultSet.getLong(10));
            return orderVO;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new OrderVO();
    }
}
