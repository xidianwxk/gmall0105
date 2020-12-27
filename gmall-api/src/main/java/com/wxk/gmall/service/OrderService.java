package com.wxk.gmall.service;

import com.wxk.gmall.bean.OmsOrder;

/**
 * @author wxk
 * @creat 2020-12-17 17:05
 */

public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId , String tradeCode);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(OmsOrder omsOrder);
}
