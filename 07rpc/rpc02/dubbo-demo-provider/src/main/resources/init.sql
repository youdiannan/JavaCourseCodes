CREATE DATABASE IF NOT EXISTS tcc_demo;

USE tcc_demo;

# 账户表
CREATE TABLE account (
    id BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    userId BIGINT(20) NOT NULL,
    currencyType VARCHAR(10) NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0,
    freezeAmount DECIMAL (10, 2) NOT NULL DEFAULT 0,
    KEY `IDX_USER_ACCOUNT` (`userId`, `currencyType`)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO account(userId, currencyType, balance) VALUES (1, 'CNY', 700), (1, 'USD', 100), (2, 'CNY', 700), (2, 'USD', 100);

# 冻结资产表
CREATE TABLE account_freeze (
    id BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tradeId BIGINT(20) NOT NULL COMMENT '分布式id',
    fromUser BIGINT(20) NOT NULL,
    toUser BIGINT(20) NOT NULL,
    fromCurrency VARCHAR(10) NOT NULL,
    toCurrency VARCHAR(10) NOT NULL,
    fromAmount DECIMAL(10,2) NOT NULL,
    toAmount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-交易未完成，1-交易已完成，2-交易作废',
    KEY `IDX_TRADE` (`tradeId`)
) DEFAULT CHARSET=utf8mb4;