package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteAttr(String id ) throws Exception{

        try {
            //先删除 属性值表的数据
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.delete(baseAttrValue);
            //再删除属性表的记录
            baseAttrInfoMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo){
        baseAttrInfoMapper.insertSelective(baseAttrInfo);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for(BaseAttrValue baseAttrValue:attrValueList){
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }



    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);
        return select;
    }



}
