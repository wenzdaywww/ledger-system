-- 数据字典数据
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('Sex', '性别', 'K1', '1', '男', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('Sex', '性别', 'K0', '0', '女', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('UserState', '用户状态', 'K1', '1', '有效', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('UserState', '用户状态', 'K2', '2', '注销', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('UserState', '用户状态', 'K3', '3', '封号', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('YesOrNo', '是否', 'K1', '1', '是', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('YesOrNo', '是否', 'K0', '0', '否', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('ShopPlatform', '店铺平台', 'K1', '1', '拼多多', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('ShopPlatform', '店铺平台', 'K2', '2', '淘宝', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('ShopState', '店铺状态', 'K1', '1', '有效', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('ShopState', '店铺状态', 'K0', '0', '注销', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('GoodsState', '商品状态', 'K0', '0', '删除', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('GoodsState', '商品状态', 'K1', '1', '上架', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('GoodsState', '商品状态', 'K2', '2', '下架', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K0', '0', '待确认', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K1', '1', '未支付，交易关闭', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K2', '2', '未发货，退款成功', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K3', '3', '已发货，待签收', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K4', '4', '交易成功', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K5', '5', '退货退款', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K6', '6', '仅退款', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K7', '7', '刷单', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K8', '8', '待发货', '1');
INSERT INTO `CODE_DATA` (`CODE_TYPE`, `CODE_NAME`, `CODE_KEY`, `CODE_VALUE`, `VALUE_NAME`, `IS_VALID`) VALUES ('OrderState', '订单状态', 'K9', '9', '待申请退款', '1');
-- 角色信息
INSERT INTO `SYS_ROLE` (`ROLE_CODE`, `ROLE_NAME`, `UPDATE_TIME`, `CREATE_TIME`) VALUES ('ROLE_USER', '普通用户', now(), now());
-- 角色访问权限信息
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/user/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/book/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/code/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/goods/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/order/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/sales/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
INSERT INTO `AUTHORITY_ROLE` (`URL`, `ROLE_ID`, `IS_VALID`, `UPDATE_TIME`, `CREATE_TIME`)
VALUES ('/shop/*', (SELECT R.ROLE_ID FROM SYS_ROLE R WHERE R.ROLE_CODE='ROLE_USER'), '1', now(), now());
