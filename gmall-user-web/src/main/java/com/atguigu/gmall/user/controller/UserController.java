package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {

    @Reference
    UserService userService;

    @RequestMapping("userInfoList")
    public ResponseEntity<List<UserInfo>> userInfoList(HttpServletRequest request){

        List<UserInfo> userInfoList = userService.userInfoList();

        return ResponseEntity.ok(userInfoList);
    }

    @RequestMapping("getAddressListByUser")
    public ResponseEntity<List<UserAddress>> getAddressListByUser(String userId){
        List<UserAddress> userAddressList = userService.getUserAddressList(userId);

        return ResponseEntity.ok(userAddressList);
    }
}
