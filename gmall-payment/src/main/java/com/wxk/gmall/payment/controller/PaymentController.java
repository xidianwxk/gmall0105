package com.wxk.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wxk.gmall.annotations.LoginRequired;
import com.wxk.gmall.bean.OmsOrder;
import com.wxk.gmall.bean.PaymentInfo;
import com.wxk.gmall.payment.config.AlipayConfig;
import com.wxk.gmall.service.OrderService;
import com.wxk.gmall.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxk
 * @creat 2020-12-24 10:41
 */


@Controller
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymentService paymentService;

    @Reference
    OrderService orderService;


    @RequestMapping("alipay/callback/return")
    @LoginRequired(loginSuccess = true)
    public String aliPayCallBackReturn(HttpServletRequest request, ModelMap modelMap){

        // 回调请求中获取支付宝参数
        //签名
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");    //交易凭证
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();


        // 通过支付宝的paramsMap进行签名验证，2.0版本的接口将paramsMap参数去掉了，导致同步请求没法验签
        if(StringUtils.isNotBlank(sign)){
            // 验签成功
            // 幂等性检查   updatePayment（实现）
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setAlipayTradeNo(trade_no);// 支付宝的交易凭证号
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());
            // 更新用户的支付状态
            paymentService.updatePayment(paymentInfo);

        }

        //支付成功后  引起一系列 的系统服务   订单服务更新（订单号）    库存  物流
        //消息队列    单向
        // 向消息中间件发送一个检查支付状态(支付服务消费)的延迟消息队列
        //paymentService.sendDelayPaymentResultCheckQueue(outTradeNo,5);

        return "finish";
    }

    @RequestMapping("mx/submit")
    @LoginRequired(loginSuccess = true)
    public String mx(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap){

        return null;
    }

    @RequestMapping("alipay/submit")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap){

        // 获得一个支付宝请求的客户端(它并不是一个链接，而是一个封装好的http的表单请求)
        String form = null;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        // 回调函数
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outTradeNo);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        //map.put("total_amount",totalAmount);
        map.put("total_amount",0.01);
        map.put("subject","尚硅谷感光徕卡Pro300瞎命名系列手机");

        String param = JSON.toJSONString(map);

        alipayRequest.setBizContent(param);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        //生成并保存用户的支付信息
        OmsOrder omsOrder = orderService.getOrderByOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(omsOrder.getId());
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject("谷粒商城商品一件");
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);

        //向消息中间件发送一个检查支付状态（支付服务消息）的延迟消息队列   《=》定时任务 ：不够灵活
        //支付结束    解除延迟任务/设置新的延迟任务
        paymentService.sendDelayPaymentResultCheckQueue(outTradeNo, 5);    //检查当前订单的支付状态
        //循环检查    当前订单的支付状态

        // 支付成功      更新订单状态    （重复更新问题 => 幂等性检查  服务器相同的写请求 只做一次  ）

        //提交请求到支付宝
        return form;
    }

    @RequestMapping("index")
    @LoginRequired(loginSuccess = true)
    public String index(String outTradeNo , BigDecimal totalAmount , HttpServletRequest request , ModelMap modelMap){


        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        modelMap.put("nickname",nickname);
        modelMap.put("outTradeNo",outTradeNo);
        modelMap.put("totalAmount",totalAmount);
        return "index";
    }
}
