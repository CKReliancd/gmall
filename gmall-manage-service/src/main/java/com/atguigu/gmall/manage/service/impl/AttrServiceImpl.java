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
    public List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);

        for (BaseAttrInfo attrInfo : select) {
            //通过基本属性信息的id去查基本属性值，返回集合
            String attrId = attrInfo.getId();
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(attrId);
            List<BaseAttrValue> select1 = baseAttrValueMapper.select(baseAttrValue);
            //把select1通过attrInfo写入select
            attrInfo.setAttrValueList(select1);
        }
        return select;
    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {

        baseAttrInfoMapper.insertSelective(baseAttrInfo);

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        for (BaseAttrValue baseAttrValue : attrValueList) {
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    @Override
    public void updateAttr(BaseAttrInfo baseAttrInfo) {
        //先删除属性值表的数据
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        String id = baseAttrInfo.getId();
        baseAttrValue.setAttrId(id);
        baseAttrValueMapper.delete(baseAttrValue);
        //更新属性表中的数据
        baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        for (BaseAttrValue attrValue : attrValueList) {
            attrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(attrValue);
        }


    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteAttr(String id) throws Exception {

        try {
            //先删除属性值表的数据
            BaseAttrValue baseAttrValue = new BaseAttrValue();

            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.delete(baseAttrValue);
            //再删除属性表的记录
            baseAttrInfoMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw e;
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
