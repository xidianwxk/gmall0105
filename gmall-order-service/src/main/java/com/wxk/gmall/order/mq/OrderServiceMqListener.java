package com.wxk.gmall.order.mq;


import com.wxk.gmall.bean.OmsOrder;
import com.wxk.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderServiceMqListener {

    @Autowired
    OrderService orderService;

    //监听消息
    @JmsListener(destination = "PAYHMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentResult(MapMessage mapMessage) throws JMSException {

        String out_trade_no = mapMessage.getString("out_trade_no");

        // 更新订单状态业务
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(out_trade_no);
        orderService.updateOrder(omsOrder);

        System.out.println("**********************************");
    }
}
