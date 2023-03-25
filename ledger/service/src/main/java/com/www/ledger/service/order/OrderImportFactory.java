package com.www.ledger.service.order;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.utils.SpringUtils;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.service.order.impl.PddImportServiceImpl;
import com.www.ledger.service.order.impl.TaobaoImportServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>@Description 订单导入数据处理工厂 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/23 22:00 </p>
 */
public class OrderImportFactory {
    /**
     * <p>@Description 获取订单导入数据处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/23 22:21 </p>
     * @param shopType 店铺平台
     * @return 订单导入数据处理对象实例
     */
    public static IOrderImportService getOrderImport(String shopType){
        //拼多多
        if (StringUtils.equalsAnyIgnoreCase(shopType, CodeDict.getValue(CodeTypeEnum.ShopPlatform_Pdd.getType(), CodeTypeEnum.ShopPlatform_Pdd.getKey()))){
            return SpringUtils.getApplicationContext().getBean(PddImportServiceImpl.class);
        }//淘宝
        else if(StringUtils.equalsAnyIgnoreCase(shopType, CodeDict.getValue(CodeTypeEnum.ShopPlatform_Taobao.getType(),CodeTypeEnum.ShopPlatform_Taobao.getKey()))){
            return SpringUtils.getApplicationContext().getBean(TaobaoImportServiceImpl.class);
        }else {
            throw new BusinessException("店铺平台错误");
        }
    }
}
