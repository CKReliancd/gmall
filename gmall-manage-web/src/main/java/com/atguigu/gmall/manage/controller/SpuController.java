package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.manage.util.MyUploadUtil;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("fileUpload")
    @ResponseBody
    //@RequestParam("file")：在HTML中的input的file对象的类型，和Easyui中file的类型不同
    //这就需要一个中间类型转化，所以封装参数的时候用名字对应，能100%把页面上的东西封装进入参数里面
    public String fileUpload(@RequestParam("file") MultipartFile file){
        //fdfs的上传工具
        String imgUrl = MyUploadUtil.uploadImage(file);
        return imgUrl;
    }

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
