package io.kimmking.dubbo.demo.consumer;

import io.kimmking.dubbo.demo.api.AccountService;
import io.kimmking.dubbo.demo.api.CurrencyExchangeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.stereotype.Service;

@Service
public class CurrencyAccountServiceImpl implements CurrencyAccountService{

    @DubboReference(version = "1.0.0", timeout = 1000000)
    private AccountService accountService;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean exchange(CurrencyExchangeDTO exchangeDTO) {
        boolean buyerTry = accountService.exchangeCurrency(exchangeDTO);

        boolean sellerTry = accountService.exchangeCurrency(reverseTrade(exchangeDTO));
        if (buyerTry && sellerTry) {
            return true;
        }
        throw new HmilyRuntimeException("exchange failed");
    }

    private CurrencyExchangeDTO reverseTrade(CurrencyExchangeDTO exchangeDTO) {
        CurrencyExchangeDTO reversedTrade = new CurrencyExchangeDTO();
        reversedTrade.setTradeId(exchangeDTO.getTradeId());
        reversedTrade.setBuyer(exchangeDTO.getSeller());
        reversedTrade.setBuyerCurrencyType(exchangeDTO.getSellerCurrencyType());
        reversedTrade.setBuyAmount(exchangeDTO.getSellAmount());
        reversedTrade.setSeller(exchangeDTO.getBuyer());
        reversedTrade.setSellerCurrencyType(exchangeDTO.getBuyerCurrencyType());
        reversedTrade.setSellAmount(exchangeDTO.getBuyAmount());

        return reversedTrade;
    }

    public boolean confirm(CurrencyExchangeDTO exchangeDTO) {
        System.out.println("==================");
        System.out.println("confirm");
        System.out.println("==================");
        return true;
    }

    public boolean cancel(CurrencyExchangeDTO exchangeDTO) {
        System.out.println("==================");
        System.out.println("cancel");
        System.out.println("==================");
        return true;
    }
}
