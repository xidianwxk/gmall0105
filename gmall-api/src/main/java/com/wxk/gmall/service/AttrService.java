package com.wxk.gmall.service;

import com.wxk.gmall.bean.PmsBaseAttrInfo;
import com.wxk.gmall.bean.PmsBaseAttrValue;
import com.wxk.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 * @author wxk
 * @creat 2020-10-27 21:15
 */


public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
