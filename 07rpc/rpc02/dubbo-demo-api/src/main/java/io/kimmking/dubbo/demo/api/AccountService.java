package io.kimmking.dubbo.demo.api;

import org.dromara.hmily.annotation.Hmily;

public interface AccountService {

    @Hmily
    boolean exchangeCurrency(CurrencyExchangeDTO exchangeDTO);

}
