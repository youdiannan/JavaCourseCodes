package io.kimmking.dubbo.demo.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountPO {

    private Long id;

    private Long userId;

    private String currencyType;

    private BigDecimal balance;

    private BigDecimal freezeAmount;
}
