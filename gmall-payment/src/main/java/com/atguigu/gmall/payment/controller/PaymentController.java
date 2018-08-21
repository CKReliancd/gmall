package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.bean.OrderInfo;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.payment.config.AlipayConfig;
import com.atguigu.gmall.payment.service.PaymentService;
import com.atguigu.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;

@Controller
public class PaymentController {


    @Reference
    OrderService orderService;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymentService paymentService;

    @RequestMapping("alipay/callback/return")
    public String callbackReturn(String orderId, ModelMap map){
        //修改支付信息



        return "testPaySuccess";
    }

    //@LoginRequire(ifNeedSuccess = true)
    @RequestMapping("alipay/submit")
    @ResponseBody
    public String alipay(String orderId, ModelMap map){

        String userId = "4";

        OrderInfo order = orderService.getOrderById(orderId);

        // 生成和保存支付信息
        PaymentInfo paymentInfo = new PaymentInfo();

        paymentInfo.setOutTradeNo(order.getOutTradeNo());
        paymentInfo.setPaymentStatus("未支付");
        paymentInfo.setOrderId(orderId);
        paymentInfo.setTotalAmount(order.getTotalAmount());
        paymentInfo.setSubject(order.getOrderDetailList().get(0).getSkuName());
        paymentInfo.setCreateTime(new Date());

        paymentService.savePayment(paymentInfo);

        // 重定向到支付宝平台
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);//在公共参数中设置回跳和通知地址

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("out_trade_no",order.getOutTradeNo());
        stringObjectHashMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        stringObjectHashMap.put("total_amount",0.001);//orderById.getTotalAmount()
        stringObjectHashMap.put("subject","测试硅谷手机phone");

        String json = JSON.toJSONString(stringObjectHashMap);
        alipayRequest.setBizContent(json);

        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return form;
    }

    //@LoginRequire(ifNeedSuccess = true)
    @RequestMapping("index")
    public String index(String orderId, ModelMap map){

        String userId = "4";

        OrderInfo orderInfo = orderService.getOrderById(orderId);

        map.put("orderId",orderId);
        map.put("outTradeNo",orderInfo.getOutTradeNo());
        map.put("totalAmount",orderInfo.getTotalAmount());
        return "index";
    }

}
