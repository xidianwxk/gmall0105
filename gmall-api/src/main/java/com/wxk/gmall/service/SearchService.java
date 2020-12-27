package com.wxk.gmall.service;

import com.wxk.gmall.bean.PmsSearchParam;
import com.wxk.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-11-17 20:41
 */

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
