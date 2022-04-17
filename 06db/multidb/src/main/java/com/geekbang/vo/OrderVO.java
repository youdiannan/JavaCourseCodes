package com.geekbang.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {

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

}
