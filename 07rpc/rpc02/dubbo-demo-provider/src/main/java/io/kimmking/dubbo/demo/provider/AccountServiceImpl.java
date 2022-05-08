package io.kimmking.dubbo.demo.provider;

import io.kimmking.dubbo.demo.api.AccountService;
import io.kimmking.dubbo.demo.api.CurrencyExchangeDTO;
import io.kimmking.dubbo.demo.provider.mapper.AccountMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(version = "1.0.0")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean exchangeCurrency(CurrencyExchangeDTO exchangeDTO) {
        // 冻结账户金额
        int count = accountMapper.freeze(exchangeDTO.getBuyer(), exchangeDTO.getBuyerCurrencyType(), exchangeDTO.getBuyAmount());
        if (count < 1) {
            throw new HmilyRuntimeException("freeze failed");
        }
        return true;
    }

    @Transactional
    public boolean confirm(CurrencyExchangeDTO exchangeDTO) {
        int release = accountMapper.release(exchangeDTO.getBuyer(), exchangeDTO.getBuyerCurrencyType(), exchangeDTO.getBuyAmount());
        if (release < 1) {
            throw new HmilyRuntimeException("release failed");
        }
        accountMapper.increase(exchangeDTO.getBuyer(), exchangeDTO.getSellerCurrencyType(), exchangeDTO.getSellAmount());
        return true;
    }

    @Transactional
    public boolean cancel(CurrencyExchangeDTO exchangeDTO) {
        int release = accountMapper.release(exchangeDTO.getBuyer(), exchangeDTO.getBuyerCurrencyType(), exchangeDTO.getBuyAmount());
        if (release < 1) {
            throw new HmilyRuntimeException("release failed");
        }
        accountMapper.increase(exchangeDTO.getBuyer(), exchangeDTO.getBuyerCurrencyType(), exchangeDTO.getBuyAmount());
        return true;
    }
}
