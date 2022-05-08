package io.kimmking.dubbo.demo.consumer;

import io.kimmking.dubbo.demo.api.CurrencyExchangeDTO;

public interface CurrencyAccountService {

    boolean exchange(CurrencyExchangeDTO exchangeDTO);
}
