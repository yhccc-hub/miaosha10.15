package com.miaoshaProject.service.model;


import java.math.BigDecimal;

/**
 * 首先考虑模型，然后再考虑数据库
 * 用户下单的交易模型
 */

public class OrderModel {
    //订单id
    private String id;

    //用户id
    private Integer userId;

    //购买商品id
    private Integer itemId;

    //若非空，则表示是以秒杀商品的方式下单
    private Integer promoId;

    //购买数量
    private Integer amount;

    //购买单价,若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;

    //购买金额
    private BigDecimal orderPrice;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
