package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.OrderModel;

public interface OrderService {
    // 使用第一种
    //1通过前端url穿过来秒杀活动的id，然后下单接口内校验对应id是否属于对应商品且活动一开始
    //2直接在下单接口内判断对应的商品是否存在秒杀活动，若存在，进行中的则以秒杀价格下单
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
