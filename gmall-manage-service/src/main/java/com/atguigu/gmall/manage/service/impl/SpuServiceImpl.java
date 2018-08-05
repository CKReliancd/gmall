package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.BaseSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.SpuImageMapper;
import com.atguigu.gmall.manage.mapper.SpuInfoMapper;
import com.atguigu.gmall.manage.mapper.SpuSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


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
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public void saveSpu(SpuInfo spuInfo){

        //保存

    }
    /*
    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo){
        baseAttrInfoMapper.insertSelective(baseAttrInfo);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for(BaseAttrValue baseAttrValue:attrValueList){
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }
    */



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
