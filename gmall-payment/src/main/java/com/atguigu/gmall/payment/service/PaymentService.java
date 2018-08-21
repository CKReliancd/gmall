package com.atguigu.gmall.payment.service;

import com.atguigu.gmall.bean.PaymentInfo;

public interface PaymentService {
    void savePayment(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);
}
