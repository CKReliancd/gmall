package com.atguigu.gmall.list.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParam;
import com.atguigu.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ListController {

    @Reference
    ListService listService;

    @RequestMapping("list.html")
    public String search(SkuLsParam skuLsParam, ModelMap map){

        List<SkuLsInfo> skuLsInfo;

        skuLsInfo = listService.search(skuLsParam);

        map.put("skuLsInfo",skuLsInfo);
        return "list";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
