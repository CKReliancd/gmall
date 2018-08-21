package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.OrderDetail;
import com.atguigu.gmall.bean.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    public String genTradeCode(String userId) {
        String k = "user:"+userId+":tradeCode";
        //javaJDK提供的一个自动生成主键的方法,全局唯一标识符,在同一时空中的所有机器都是唯一的
        String v = UUID.randomUUID().toString();

        Jedis jedis = redisUtil.getJedis();
        jedis.setex(k,60*30,v);

        jedis.close();

        return v;
    }

    @Override
    public boolean checkTradeCode(String tradeCode, String userId) {
        String k = "user:"+userId+":tradeCode";

        //在计算机中，不确定程序是否执行，默认执行，申明变量boolean默认false；
        boolean b = false;

        Jedis jedis = redisUtil.getJedis();

        //根据key用get在redis里面找值，如果找到了，则通过，删除redis里面对应的tradeCode
        String s = jedis.get(k);

        if(StringUtils.isNotBlank(s)&&s.equals(tradeCode)){
            b = true;
            jedis.del(k);
        }


        return b;
    }

    @Override
    public String saveOrder(OrderInfo orderInfo) {
        orderInfoMapper.insertSelective(orderInfo);
        String orderId = orderInfo.getId();

        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderId(orderId);
            orderDetailMapper.insertSelective(orderDetail);
        }

        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderById(String orderId) {

        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setId(orderId);
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderInfo1);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> select = orderDetailMapper.select(orderDetail);

        orderInfo.setOrderDetailList(select);
        return orderInfo;
    }
}
