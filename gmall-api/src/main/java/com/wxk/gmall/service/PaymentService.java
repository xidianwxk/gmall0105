package com.wxk.gmall.service;

import com.wxk.gmall.bean.PaymentInfo;

/**
 * @author wxk
 * @creat 2020-12-24 13:31
 */

public interface PaymentService {
    void updatePayment(PaymentInfo paymentInfo);

    void savePaymentInfo(PaymentInfo paymentInfo);
}
