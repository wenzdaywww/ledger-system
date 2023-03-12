-- 1、用户店铺信息
CREATE TABLE IF NOT EXISTS USER_SHOP (
    SHOP_ID                   BIGINT          PRIMARY KEY AUTO_INCREMENT          COMMENT '店铺主键ID',
    SHOP_NAME                 VARCHAR(40)                                         COMMENT '店铺名',
    SHOP_TYPE                 VARCHAR(2)                                          COMMENT '店铺平台(code=ShopPlatform)',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    SHOP_STATE                CHAR(1)                                             COMMENT '店铺状态(code=ShopState)：1有效，0注销',
    UPDATE_TIME               DATETIME                                            COMMENT '更新时间',
    CREATE_TIME               DATETIME                                            COMMENT '创建时间'
);
ALTER TABLE     USER_SHOP           COMMENT '用户店铺信息';
CREATE INDEX    INDEX_SHOP_NAME     ON  USER_SHOP (SHOP_NAME);
CREATE INDEX    INDEX_USER_ID       ON  USER_SHOP (USER_ID);
CREATE INDEX    INDEX_SHOP_STATE    ON  USER_SHOP (SHOP_STATE);
-- 2、店铺商品信息
CREATE TABLE IF NOT EXISTS SHOP_GOODS (
    SG_ID                     BIGINT          PRIMARY KEY AUTO_INCREMENT          COMMENT '商品主键ID',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    GOODS_NAME                VARCHAR(256)                                        COMMENT '商品名称',
    SHOP_ID                   BIGINT                                              COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    GOODS_STATE               CHAR(1)                                             COMMENT '商品状态(code=GoodsState)：0删除，1上架，2下架',
    UPDATE_TIME               DATETIME                                            COMMENT '更新时间',
    CREATE_TIME               DATETIME                                            COMMENT '创建时间'
);
ALTER TABLE     SHOP_GOODS          COMMENT '店铺商品信息';
CREATE INDEX    INDEX_GOODS_ID      ON  SHOP_GOODS (GOODS_ID);
CREATE INDEX    INDEX_SHOP_ID       ON  SHOP_GOODS (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  SHOP_GOODS (USER_ID);
CREATE INDEX    INDEX_GOODS_STATE   ON  SHOP_GOODS (GOODS_STATE);
-- 3、商品样式信息
CREATE TABLE IF NOT EXISTS GOODS_STYLE (
    STYLE_ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT          COMMENT '商品样式主键ID',
    STYLE_NAME                VARCHAR(125)                                        COMMENT '商品样式名称',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    SHOP_ID                   BIGINT                                              COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    PRICE                     DECIMAL(6,2)                                        COMMENT '价格',
    COST                      DECIMAL(6,2)                                        COMMENT '成本',
    PROFIT                    DECIMAL(6,2)                                        COMMENT '毛利润',
    RATE                      DECIMAL(6,2)                                        COMMENT '毛利率',
    UPDATE_TIME               DATETIME                                            COMMENT '更新时间',
    CREATE_TIME               DATETIME                                            COMMENT '创建时间'
);
ALTER TABLE     GOODS_STYLE          COMMENT '商品样式信息';
CREATE INDEX    INDEX_STYLE_NAME     ON  GOODS_STYLE (STYLE_NAME);
CREATE INDEX    INDEX_GOODS_ID       ON  GOODS_STYLE (GOODS_ID);
CREATE INDEX    INDEX_SHOP_ID        ON  GOODS_STYLE (SHOP_ID);
CREATE INDEX    INDEX_USER_ID        ON  GOODS_STYLE (USER_ID);
-- 4、订单信息
CREATE TABLE IF NOT EXISTS ORDER_INFO (
    ORDER_ID                  VARCHAR(40)          PRIMARY KEY                    COMMENT '订单主键ID',
    SHOP_ID                   BIGINT                                              COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    ORDER_DATE                DATE                                                COMMENT '订单日期',
    SUPPLY_ID                 VARCHAR(40)                                         COMMENT '1688订单ID',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    GOODS_NAME                VARCHAR(256)                                        COMMENT '商品名称',
    ORDER_STATE               VARCHAR(2)                                          COMMENT '订单状态(code=OrderState)',
    PRICE                     DECIMAL(10,2)                                       COMMENT '商品总价',
    PAYMENT                   DECIMAL(10,2)                                       COMMENT '实付金额',
    COST                      DECIMAL(10,2)                                       COMMENT '商品成本',
    PAYOUT                    DECIMAL(10,2)                                       COMMENT '其他支出',
    TOTAL_COST                DECIMAL(10,2)                                       COMMENT '总成本',
    PROFIT                    DECIMAL(10,2)                                       COMMENT '毛利润',
    RATE                      DECIMAL(10,2)                                       COMMENT '毛利率',
    REMARK                    VARCHAR(256)                                        COMMENT '备注',
    UPDATE_TIME               DATETIME                                            COMMENT '更新时间',
    CREATE_TIME               DATETIME                                            COMMENT '创建时间'
);
ALTER TABLE     ORDER_INFO          COMMENT '订单信息';
CREATE INDEX    INDEX_SHOP_ID       ON  ORDER_INFO (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  ORDER_INFO (USER_ID);
CREATE INDEX    INDEX_ORDER_DATE    ON  ORDER_INFO (ORDER_DATE);
CREATE INDEX    INDEX_SUPPLY_ID     ON  ORDER_INFO (SUPPLY_ID);
CREATE INDEX    INDEX_ORDER_STATE   ON  ORDER_INFO (ORDER_STATE);
CREATE INDEX    INDEX_GOODS_ID      ON  ORDER_INFO (GOODS_ID);
-- 5、月销售额信息
CREATE TABLE IF NOT EXISTS MONTH_SALES (
    MB_ID                     BIGINT          PRIMARY KEY                         COMMENT '月账单主键ID',
    SHOP_ID                   BIGINT                                              COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    MONTH_DATE                DATE                                                COMMENT '账单月份',
    ORDER_QUANTITY            BIGINT                                              COMMENT '月订单数',
    ORDER_SUCCEED             BIGINT                                              COMMENT '月成交单数',
    ORDER_FAILED              BIGINT                                              COMMENT '月失败单数',
    SALE                      DECIMAL(20,2)                                       COMMENT '月销售额',
    COST                      DECIMAL(20,2)                                       COMMENT '月成本费',
    PROMOTION                 DECIMAL(20,2)                                       COMMENT '推广费',
    SERVICE                   DECIMAL(20,2)                                       COMMENT '服务费',
    PROFIT                    DECIMAL(20,2)                                       COMMENT '月毛利润',
    RATE                      DECIMAL(10,2)                                       COMMENT '月毛利率',
    UPDATE_TIME               DATETIME                                            COMMENT '更新时间',
    CREATE_TIME               DATETIME                                            COMMENT '创建时间'
);
ALTER TABLE     MONTH_SALES          COMMENT '月销售额信息';
CREATE INDEX    INDEX_SHOP_ID       ON  MONTH_SALES (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  MONTH_SALES (USER_ID);
CREATE INDEX    INDEX_MONTH_DATE    ON  MONTH_SALES (MONTH_DATE);














