package com.wxk.gmall.service;

import com.wxk.gmall.bean.PmsBaseCatalog1;
import com.wxk.gmall.bean.PmsBaseCatalog2;
import com.wxk.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-27 16:10
 */

public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
