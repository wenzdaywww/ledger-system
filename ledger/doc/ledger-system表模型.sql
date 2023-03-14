-- 1、用户店铺信息
CREATE TABLE IF NOT EXISTS USER_SHOP (
    SHOP_ID                   INT          PRIMARY KEY AUTO_INCREMENT             COMMENT '店铺主键ID',
    SHOP_NAME                 VARCHAR(40)                                         COMMENT '店铺名',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    SHOP_TYPE                 VARCHAR(2)                                          COMMENT '店铺平台(code=ShopPlatform)',
    SHOP_STATE                CHAR(1)      DEFAULT '1'                            COMMENT '店铺状态(code=ShopState)：1有效，0注销',
    UPDATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '更新时间',
    CREATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '创建时间'
);
ALTER TABLE     USER_SHOP           COMMENT '用户店铺信息';
CREATE INDEX    INDEX_SHOP_NAME     ON  USER_SHOP (SHOP_NAME);
CREATE INDEX    INDEX_USER_ID       ON  USER_SHOP (USER_ID);
CREATE INDEX    INDEX_SHOP_STATE    ON  USER_SHOP (SHOP_STATE);
-- 2、用户账簿信息
CREATE TABLE IF NOT EXISTS USER_BOOK (
    UB_ID                     INT              PRIMARY KEY AUTO_INCREMENT         COMMENT '用户账簿主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    RETAINED_PROFITS          DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '净利润',
    RETAINED_PROFITS_RATE     DECIMAL(10,2)    DEFAULT 0.0                        COMMENT '净利率',
    GROSS_PROFIT              DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '毛利润',
    GROSS_PROFIT_RATE         DECIMAL(10,2)    DEFAULT 0.0                        COMMENT '毛利率',
    TOTAL_ORDER               INT              DEFAULT 0                          COMMENT '总订单数',
    SUCCEED_ORDER             INT              DEFAULT 0                          COMMENT '总成交单数',
    FAILED_ORDER              INT              DEFAULT 0                          COMMENT '总失败单数',
    SALE_AMOUNT               DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '总销售额',
    COST_AMOUNT               DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '总成本费',
    ADVERT_AMOUNT             DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '总推广费',
    SERVICE_AMOUNT            DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '总服务费',
    VIRTUAL_AMOUNT            DECIMAL(20,2)    DEFAULT 0.0                        COMMENT '总刷单费',
    UPDATE_TIME               DATETIME         DEFAULT NOW()                      COMMENT '更新时间',
    CREATE_TIME               DATETIME         DEFAULT NOW()                      COMMENT '创建时间'
);
ALTER TABLE     USER_BOOK           COMMENT '用户账簿信息';
CREATE INDEX    INDEX_USER_ID       ON  USER_BOOK (USER_ID);
-- 3、店铺销售额信息
CREATE TABLE IF NOT EXISTS SHOP_SALES (
    SS_ID                     INT               PRIMARY KEY AUTO_INCREMENT        COMMENT '店铺销售额主键ID',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    RETAINED_PROFITS          DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店净利润',
    RETAINED_PROFITS_RATE     DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '店净利率',
    GROSS_PROFIT              DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店毛利润',
    GROSS_PROFIT_RATE         DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '店毛利率',
    TOTAL_ORDER               INT               DEFAULT 0                         COMMENT '店订单数',
    SUCCEED_ORDER             INT               DEFAULT 0                         COMMENT '店成交单数',
    FAILED_ORDER              INT               DEFAULT 0                         COMMENT '店失败单数',
    SALE_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店销售额',
    COST_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店成本费',
    ADVERT_AMOUNT             DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店推广费',
    SERVICE_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店服务费',
    VIRTUAL_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '店刷单费',
    UPDATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '更新时间',
    CREATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '创建时间'
);
ALTER TABLE     SHOP_SALES          COMMENT '店铺销售额信息';
CREATE INDEX    INDEX_SHOP_ID       ON  SHOP_SALES (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  SHOP_SALES (USER_ID);
-- 4、店铺商品信息
CREATE TABLE IF NOT EXISTS SHOP_GOODS (
    SG_ID                     INT          PRIMARY KEY AUTO_INCREMENT             COMMENT '商品主键ID',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    GOODS_NAME                VARCHAR(256)                                        COMMENT '商品名称',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    GOODS_STATE               CHAR(1)      DEFAULT '1'                            COMMENT '商品状态(code=GoodsState)：0删除，1上架，2下架',
    UPDATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '更新时间',
    CREATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '创建时间'
);
ALTER TABLE     SHOP_GOODS          COMMENT '店铺商品信息';
CREATE INDEX    INDEX_GOODS_ID      ON  SHOP_GOODS (GOODS_ID);
CREATE INDEX    INDEX_SHOP_ID       ON  SHOP_GOODS (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  SHOP_GOODS (USER_ID);
CREATE INDEX    INDEX_GOODS_STATE   ON  SHOP_GOODS (GOODS_STATE);
-- 5、商品样式信息
CREATE TABLE IF NOT EXISTS GOODS_STYLE (
    STYLE_ID                  INT           PRIMARY KEY AUTO_INCREMENT            COMMENT '商品样式主键ID',
    STYLE_NAME                VARCHAR(125)                                        COMMENT '商品样式名称',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    SALE_AMOUNT               DECIMAL(6,2)  DEFAULT 0.0                           COMMENT '价格',
    COST_AMOUNT               DECIMAL(6,2)  DEFAULT 0.0                           COMMENT '成本',
    GROSS_PROFIT              DECIMAL(6,2)  DEFAULT 0.0                           COMMENT '毛利润',
    GROSS_PROFIT_RATE         DECIMAL(6,2)  DEFAULT 0.0                           COMMENT '毛利率',
    UPDATE_TIME               DATETIME      DEFAULT NOW()                         COMMENT '更新时间',
    CREATE_TIME               DATETIME      DEFAULT NOW()                         COMMENT '创建时间'
);
ALTER TABLE     GOODS_STYLE          COMMENT '商品样式信息';
CREATE INDEX    INDEX_STYLE_NAME     ON  GOODS_STYLE (STYLE_NAME);
CREATE INDEX    INDEX_GOODS_ID       ON  GOODS_STYLE (GOODS_ID);
CREATE INDEX    INDEX_SHOP_ID        ON  GOODS_STYLE (SHOP_ID);
CREATE INDEX    INDEX_USER_ID        ON  GOODS_STYLE (USER_ID);
-- 6、订单信息
CREATE TABLE IF NOT EXISTS ORDER_INFO (
    ORDER_ID                  VARCHAR(40)          PRIMARY KEY                    COMMENT '订单主键ID',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    ORDER_DATE                DATE                                                COMMENT '订单日期',
    SUPPLY_ID                 VARCHAR(40)                                         COMMENT '1688订单ID',
    GOODS_ID                  VARCHAR(40)                                         COMMENT '商品ID',
    GOODS_NAME                VARCHAR(256)                                        COMMENT '商品名称',
    ORDER_STATE               VARCHAR(2)                                          COMMENT '订单状态(code=OrderState)',
    SALE_AMOUNT               DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '商品总价',
    PAYMENT_AMOUNT            DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '实付金额',
    COST_AMOUNT               DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '商品成本',
    PAYOUT_AMOUNT             DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '其他支出',
    TOTAL_COST                DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '总成本',
    GROSS_PROFIT              DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '毛利润',
    GROSS_PROFIT_RATE         DECIMAL(10,2)        DEFAULT 0.0                    COMMENT '毛利率',
    REMARK                    VARCHAR(256)                                        COMMENT '备注',
    UPDATE_TIME               DATETIME             DEFAULT NOW()                  COMMENT '更新时间',
    CREATE_TIME               DATETIME             DEFAULT NOW()                  COMMENT '创建时间'
);
ALTER TABLE     ORDER_INFO          COMMENT '订单信息';
CREATE INDEX    INDEX_SHOP_ID       ON  ORDER_INFO (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  ORDER_INFO (USER_ID);
CREATE INDEX    INDEX_ORDER_DATE    ON  ORDER_INFO (ORDER_DATE);
CREATE INDEX    INDEX_SUPPLY_ID     ON  ORDER_INFO (SUPPLY_ID);
CREATE INDEX    INDEX_ORDER_STATE   ON  ORDER_INFO (ORDER_STATE);
CREATE INDEX    INDEX_GOODS_ID      ON  ORDER_INFO (GOODS_ID);
-- 7、月销售额信息
CREATE TABLE IF NOT EXISTS MONTH_SALES (
    MS_ID                     INT               PRIMARY KEY AUTO_INCREMENT        COMMENT '月账单主键ID',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    MONTH_DATE                DATE                                                COMMENT '账单月份',
    RETAINED_PROFITS          DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月净利润',
    RETAINED_PROFITS_RATE     DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '月净利率',
    GROSS_PROFIT              DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '月毛利润',
    GROSS_PROFIT_RATE         DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '月毛利率',
    TOTAL_ORDER               INT               DEFAULT 0                         COMMENT '月订单数',
    SUCCEED_ORDER             INT               DEFAULT 0                         COMMENT '月成交单数',
    FAILED_ORDER              INT               DEFAULT 0                         COMMENT '月失败单数',
    SALE_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月销售额',
    COST_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月成本费',
    ADVERT_AMOUNT             DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月推广费',
    SERVICE_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月服务费',
    VIRTUAL_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '月刷单费',
    UPDATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '更新时间',
    CREATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '创建时间'
);
ALTER TABLE     MONTH_SALES         COMMENT '月销售额信息';
CREATE INDEX    INDEX_SHOP_ID       ON  MONTH_SALES (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  MONTH_SALES (USER_ID);
CREATE INDEX    INDEX_MONTH_DATE    ON  MONTH_SALES (MONTH_DATE);
-- 8、年销售额信息
CREATE TABLE IF NOT EXISTS YEAR_SALES (
    YS_ID                     INT               PRIMARY KEY AUTO_INCREMENT        COMMENT '年账单主键ID',
    SHOP_ID                   INT                                                 COMMENT '店铺主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    YEAR                      YEAR                                                COMMENT '账单年份',
    RETAINED_PROFITS          DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年净利润',
    RETAINED_PROFITS_RATE     DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '年净利率',
    GROSS_PROFIT              DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '年毛利润',
    GROSS_PROFIT_RATE         DECIMAL(10,2)     DEFAULT 0.0                       COMMENT '年毛利率',
    TOTAL_ORDER               INT               DEFAULT 0.0                       COMMENT '年订单数',
    SUCCEED_ORDER             INT               DEFAULT 0.0                       COMMENT '年成交单数',
    FAILED_ORDER              INT               DEFAULT 0.0                       COMMENT '年失败单数',
    SALE_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年销售额',
    COST_AMOUNT               DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年成本费',
    ADVERT_AMOUNT             DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年推广费',
    SERVICE_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年服务费',
    VIRTUAL_AMOUNT            DECIMAL(20,2)     DEFAULT 0.0                       COMMENT '年刷单费',
    UPDATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '更新时间',
    CREATE_TIME               DATETIME          DEFAULT NOW()                     COMMENT '创建时间'
);
ALTER TABLE     YEAR_SALES          COMMENT '年销售额信息';
CREATE INDEX    INDEX_SHOP_ID       ON  YEAR_SALES (SHOP_ID);
CREATE INDEX    INDEX_USER_ID       ON  YEAR_SALES (USER_ID);
CREATE INDEX    INDEX_YEAR          ON  YEAR_SALES (YEAR);













