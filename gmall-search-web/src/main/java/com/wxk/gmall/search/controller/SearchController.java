package com.wxk.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wxk.gmall.annotations.LoginRequired;
import com.wxk.gmall.bean.*;
import com.wxk.gmall.service.AttrService;
import com.wxk.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author wxk
 * @creat 2020-11-17 20:01
 */

@Controller
public class SearchController {


    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {//三级分类id   关键字   平台属性集合

        //调用搜索服务  返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);


        //平台属性列表（抽取）   set
        //抽取检索结果包含平台属性集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        //根据valueid 查询属性列表
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);


        //对平台属性集合处理   点击改属性  对应一行删除

        String[] delValueIds = pmsSearchParam.getValueId();
        if (delValueIds != null) {

            //面包屑
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            //arrayList 删除  数组下表越界
            //Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成的参数
                pmsSearchCrumb.setValueId(delValueId);
                //pmsSearchCrumb.setValueName(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                //pmsSearchCrumbs.add(pmsSearchCrumb);

                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        //String valueName = pmsBaseAttrValue.getValueName();
                        String valueId = pmsBaseAttrValue.getId();


                        if (delValueId.equals(valueId)) {

                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除当前valueId所在属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }

//        for (PmsBaseAttrInfo pmsBaseAttrInfo : pmsBaseAttrInfos) {
//            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
//            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
//                String valueId = pmsBaseAttrValue.getId();
//                for (String delValueId : delValueIds) {
//                    if(valueId.equals(delValueId)){
//                        //删除当前valueId所在属性组
//                    }
//                }
//
//            }
//        }


        //属性列表与面包屑
        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }

        return "list";
    }


    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValueId) {

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();   //上述至少有一个
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(catalog3Id)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {

                if (!pmsSkuAttrValue.equals(delValueId)) {
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }
            }
        }


        return urlParam;
    }

    //可变参数  方法复用
    private String getUrlParam(PmsSearchParam pmsSearchParam, String... delValueId) {

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();   //上述至少有一个
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(catalog3Id)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
            }
        }


        return urlParam;
    }

    @RequestMapping("index")
    @LoginRequired(loginSuccess = false)
    public String index() {
        return "index";
    }
}
