package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {


    @Reference
    OrderService orderService;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymentService paymentService;

    @RequestMapping("alipay/callback/return")
    public String callbackReturn(HttpServletRequest request, String orderId, ModelMap map){

        Map<String,String> paramsMap = null;//将异步通知收到的所有参数都存放到map中
        boolean signVerified = false;
        try {                                                   //公钥                            //字符编码方式
            signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        if(signVerified){
            //TODO 验签成功后，按照支付结果异步通知中的描述，
            // 对支付结果中的业务内容进行二次校验，校验成功后在response中
            //返回success并继续商户自身业务处理，校验失败返回failure
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            // 返回失败页面
        }


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
