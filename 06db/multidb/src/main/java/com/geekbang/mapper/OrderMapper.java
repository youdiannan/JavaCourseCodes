package com.geekbang.mapper;

import com.geekbang.po.OrderPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO TB_MALL_ORDER (OrderTime, PayTime, DiscountAmount, ActualPrice, Buyer, CreateTime, UpdateTime) VALUES (#{orderTime}, #{payTime}, #{discountAmount}, #{actualPrice}, #{buyer}, #{createTime}, #{updateTime})")
    int insert(OrderPO po);

    @Delete("DELETE FROM TB_MALL_ORDER WHERE Id=#{orderId}")
    int delete(@Param("orderId") Long orderId);

    @Select("SELECT * FROM TB_MALL_ORDER")
    List<OrderPO> selectAll();

    @Select("SELECT * FROM TB_MALL_ORDER WHERE Id=#{orderId}")
    OrderPO selectById(@Param("orderId") Long orderId);

    @Update("UPDATE TB_MALL_ORDER SET CancelStatus=1 WHERE Id=#{orderId}")
    int cancel(@Param("orderId") Long orderId);
}
