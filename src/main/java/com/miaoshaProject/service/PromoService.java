package com.miaoshaProject.service;

import com.miaoshaProject.service.model.PromoModel;

public interface PromoService {

    //根据itemid获取即将进行的或者正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
