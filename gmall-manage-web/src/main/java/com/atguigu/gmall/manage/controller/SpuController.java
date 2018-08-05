package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("saveSpu")
    @ResponseBody
    public String saveSpu(SpuInfo spuInfo){

        spuService.saveSpu(spuInfo);
        return "success";
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> baseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrs = spuService.baseSaleAttrList();
        return baseSaleAttrs;
    }

    @RequestMapping("spuList")
    @ResponseBody
    public List<SpuInfo> spuList(String catalog3Id){
        List<SpuInfo> spuInfos = spuService.spuList(catalog3Id);
        return spuInfos;

    }

}
