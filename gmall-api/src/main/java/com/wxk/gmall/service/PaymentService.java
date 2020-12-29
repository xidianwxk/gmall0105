package com.wxk.gmall.service;

import com.wxk.gmall.bean.PaymentInfo;

import java.util.Map;

/**
 * @author wxk
 * @creat 2020-12-24 13:31
 */

public interface PaymentService {
    void updatePayment(PaymentInfo paymentInfo);

    void savePaymentInfo(PaymentInfo paymentInfo);

    void sendDelayPaymentResultCheckQueue(String outTradeNo, Integer count);

    Map<String,Object> checkAlipayPayment(String out_trade_no);
}
