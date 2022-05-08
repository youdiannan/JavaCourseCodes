package io.kimmking.dubbo.demo.provider.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface AccountMapper {

    @Update("update account set balance = balance - #{amount}, freezeAmount = freezeAmount + #{amount}" +
            " where userId = #{userId} and currencyType = #{currencyType} and balance >= #{amount}")
    int freeze(@Param("userId") Long userId, @Param("currencyType") String currencyType, @Param("amount") BigDecimal amount);

    @Update("update account set freezeAmount = freezeAmount - #{amount} " +
            "where userId = #{userId} and currencyType = #{currencyType} and freezeAmount >= #{amount}")
    int release(@Param("userId") Long userId, @Param("currencyType") String currencyType, @Param("amount") BigDecimal amount);

    @Update("update account set balance = balance + #{amount} where userId = #{userId} and currencyType = #{currencyType}")
    int increase(@Param("userId") Long userId, @Param("currencyType") String currencyType, @Param("amount") BigDecimal amount);


}
