package io.kimmking.dubbo.demo.api;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CurrencyExchangeDTO implements Serializable {

    /**
     * 交易流水号
     */
    private Long tradeId;

    /**
     * 外汇交易买方
     */
    private Long buyer;

    /**
     * 买方支付的币种
     */
    private String buyerCurrencyType;

    /**
     * 买方支付的金额
     */
    private BigDecimal buyAmount;

    /**
     * 外汇交易卖方
     */
    private Long seller;

    /**
     * 卖方售出的币种
     */
    private String sellerCurrencyType;

    /**
     * 卖方售出金额
     */
    private BigDecimal sellAmount;


}
