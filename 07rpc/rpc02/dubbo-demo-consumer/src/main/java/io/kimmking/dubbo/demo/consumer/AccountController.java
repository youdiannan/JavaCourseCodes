package io.kimmking.dubbo.demo.consumer;

import io.kimmking.dubbo.demo.api.CurrencyExchangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private CurrencyAccountService currencyAccountService;

    @GetMapping("/")
    public boolean currencyExchange() {
        CurrencyExchangeDTO dto = new CurrencyExchangeDTO();
        dto.setTradeId(System.currentTimeMillis());
        dto.setBuyer(1L);
        dto.setBuyerCurrencyType(CurrencyType.CNY);
        dto.setBuyAmount(new BigDecimal("7"));

        dto.setSeller(2L);
        dto.setSellerCurrencyType(CurrencyType.USD);
        dto.setSellAmount(new BigDecimal("1"));

        return currencyAccountService.exchange(dto);
    }

}
