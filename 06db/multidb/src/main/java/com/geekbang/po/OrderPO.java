package com.geekbang.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPO {

    private Long id;

    private Long orderTime;

    private Long payTime;

    private BigDecimal discountAmount;

    private BigDecimal actualPrice;

    private Integer orderStatus;

    private Long buyer;

    private String remark;

    private Integer cancelStatus;

    private Long cancelTime;

    private Long createTime;

    private Long updateTime;

}
