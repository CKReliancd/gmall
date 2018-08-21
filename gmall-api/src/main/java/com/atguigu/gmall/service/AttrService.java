package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;

import java.util.List;

public interface AttrService {

    List<BaseAttrInfo> getAttrList(String catalog3Id);

    void saveAttr(BaseAttrInfo baseAttrInfo);

    void deleteAttr(String id) throws Exception;

    List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id);

    void updateAttr(BaseAttrInfo baseAttrInfo);
}
