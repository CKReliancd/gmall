package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;

import java.util.List;
import java.util.Map;

public interface SpuService {
    List<SpuInfo> spuList(String catalog3Id);
    List<BaseSaleAttr> baseSaleAttrList();
    void saveSpu(SpuInfo spuInfo);

    List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Map<String, String> stringStringHashMap);
}
