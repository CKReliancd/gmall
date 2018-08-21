package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Service
public class SpuServiceImpl implements SpuService {

    //商品表
    @Autowired
    SpuInfoMapper spuInfoMapper;

    //销售属性
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public void saveSpu(SpuInfo spuInfo) {
        //保存spuInfo，返回spu的主键
        spuInfoMapper.insertSelective(spuInfo);
        String spuId = spuInfo.getId();
        //保存spu销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        //iter 生成增强for循环
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            //设置spu销售属性id
            spuSaleAttr.setSpuId(spuId);
            //插入spu销售属性
            spuSaleAttrMapper.insert(spuSaleAttr);
            List<SpuSaleAttrValue> spuSaleAttrValuesList = spuSaleAttr.getSpuSaleAttrValueList();
            //保存spu的销售属性的值
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValuesList) {
                //设置spuId进入对象
                spuSaleAttrValue.setSpuId(spuId);
                //插入
                spuSaleAttrValueMapper.insert(spuSaleAttrValue);
            }
        }
        //保存图片信息
        //获取spu图片信息列表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //通过循环设置spuId，通过id插入列表信息
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        }

    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Map<String, String> stringStringHashMap) {

        return spuSaleAttrValueMapper.selectSpuSaleAttrListCheckBySku(stringStringHashMap);
    }

    @Override
    public List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {

        return spuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }

    @Override
    public List<SpuInfo> spuList(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        List<SpuInfo> select = spuInfoMapper.select(spuInfo);
        return select;
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }


}
