# 电商基础表

# 1. 订单表
# 2. 订单商品明细表
# 3. 物流信息表
# 4. 用户表
# 5. 用户地址表
# 6. 商品信息表（SPU/SKU）
# 7. 库存表

# 订单表
# 订单表原价依据对应的sku价格计算
CREATE TABLE TB_MALL_ORDER (
    Id BIGINT NOT NULL PRIMARY KEY COMMENT '订单id, 先不自增, 以防分库分表',
    OrderTime BIGINT NOT NULL COMMENT '下单时间',
    PayTime BIGINT COMMENT '支付时间',
    DiscountAmount DECIMAL(10,2) NOT NULL COMMENT '优惠金额',
    ActualPrice DECIMAL (10, 2) NOT NULL COMMENT '实付金额',
    OrderStatus TINYINT(4) NOT NULL DEFAULT '1' COMMENT '订单状态: 1-下单未支付, 2-下单已支付(待发货), 3-运输中, 4-已签收, 5-已完成, 6-拒收, 7-申请退货, 8-退货运输中, 9-退货已签收',
    Buyer BIGINT NOT NULL COMMENT '下单用户',
    Remark VARCHAR(255) DEFAULT '' COMMENT '订单备注',
    CancelStatus TINYINT(1) NOT NULL DEFAULT '0' COMMENT '取消状态, 0-未取消, 1-取消中, 2-已取消'
    CancelTime BIGINT COMMENT '取消时间',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    KEY `IDX_BUYER` (Buyer),
    KEY `IDX_ORDER_TIME` (OrderTime),
    KEY `IDX_PAY_TIME` (PayTime)
)

# 订单商品明细表
CREATE TABLE TB_MALL_ORDER_SKU (
    Id BIGINT NOT NULL PRIMARY KEY,
    OrderId BIGINT NOT NULL COMMENT '订单id',
    SkuId BIGINT NOT NULL COMMENT '订单中skuId',
    SkuCount INT NOT NULL COMMENT '商品数量',
    SpuName VARCHAR(64) NOT NULL COMMENT '商品名称',
    SkuDesc VARCHAR(255) NOT NULL COMMENT '规格描述'
    OriginPrice DECIMAL(10,2) NOT NULL COMMENT '原价',
    ActualPrice DECIMAL(10,2) NOT NULL COMMENT '实付价格',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    KEY `IDX_ORDER` (OrderId)
)

# 订单物流信息表
CREATE TABLE TB_MALL_ORDER_EXPRESS (
    Id BIGINT NOT NULL PRIMARY KEY,
    OrderId BIGINT NOT NULL COMMENT '订单id',
    Receiver VARCHAR(32) NOT NULL COMMENT '收件人',
    Sender VARCHAR(32) NOT NULL COMMENT '发件人',
    ReceiverAddress VARCHAR(255) NOT NULL COMMENT '收件人详细地址',
    SenderAddress VARCHAR(255) NOT NULL COMMENT '发件人详细地址',
    ExpressCompany VARCHAR(16) NOT NULL DEFAULT '' COMMENT '物流公司',
    TrackingNum VARCHAR(32) NOT NULL DEFAULT '' COMMENT '物流单号',
    ExpressType TINYINT(1) NOT NULL DEFAULT 1 COMMENT '1-正向订单,2-逆向订单(退货物流)',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    KEY `IDX_ORDER_EXPRESS_TYPE` (OrderId, ExpressType)
)

# 用户表
CREATE TABLE TB_MALL_USER (
    Id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id, 不需要分库分表', 
    NickName VARCHAR(16) NOT NULL COMMENT '用户昵称',
    Account VARCHAR(32) NOT NULL COMMENT '账号',
    Password VARCHAR(32) NOT NULL COMMENT '密码',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    IDX 'IDX_ACCOUNT_PWD' (Account, Password)
)

# 用户地址表
CREATE TABLE TB_MALL_USER_ADDRESS (
    Id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    UserId BIGINT NOT NULL COMMENT '用户id',
    AddressDetail VARCHAR(255) NOT NULL COMMENT '详细地址',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    KEY `IDX_USER` (UserId)
)

# SPU表
CREATE TABLE TB_MALL_SPU (
    Id BIGINT NOT NULL COMMENT '商品id',
    SpuName VARCHAR(64) NOT NULL COMMENT '商品名称',
    SpuDesc TEXT NOT NULL COMMENT '商品描述信息',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间'
)

# SKU表
CREATE TABLE TB_MALL_SKU (
    Id BIGINT NOT NULL COMMENT 'Sku id',
    SpuId BIGINT NOT NULL,
    SkuDesc VARCHAR(255) NOT NULL COMMENT 'sku规格描述',
    OriginPrice DECIMAL(10,2) NOT NULL COMMENT '原价',
    DiscountPrice DECIMAL(10,2) NOT NULL COMMENT '优惠价',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间',
    KEY `IDX_SPU` (SpuId)
)

# 库存表
CREATE TABLE TB_MALL_STOCK (
    Id BIGINT NOT NULL,
    SkuId BIGINT NOT NULL,
    StockCount INT NOT NULL COMMENT '库存值',
    CreateTime BIGINT NOT NULL COMMENT '记录创建时间',
    UpdateTime BIGINT NOT NULL COMMENT '记录更新时间'
)