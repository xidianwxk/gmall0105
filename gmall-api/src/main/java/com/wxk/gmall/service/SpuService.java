package com.wxk.gmall.service;

import com.wxk.gmall.bean.PmsProductImage;
import com.wxk.gmall.bean.PmsProductInfo;
import com.wxk.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-28 20:54
 */

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
