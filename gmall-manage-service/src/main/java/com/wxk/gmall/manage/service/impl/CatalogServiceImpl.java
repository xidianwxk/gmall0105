package com.wxk.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wxk.gmall.bean.PmsBaseCatalog1;
import com.wxk.gmall.bean.PmsBaseCatalog2;
import com.wxk.gmall.bean.PmsBaseCatalog3;
import com.wxk.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.wxk.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.wxk.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.wxk.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-27 16:11
 */

@Service
public class CatalogServiceImpl  implements CatalogService {


    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2s;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {

        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
        return pmsBaseCatalog3s;
    }
}
