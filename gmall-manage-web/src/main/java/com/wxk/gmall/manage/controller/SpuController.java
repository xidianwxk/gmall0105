package com.wxk.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wxk.gmall.bean.PmsProductImage;
import com.wxk.gmall.bean.PmsProductInfo;
import com.wxk.gmall.bean.PmsProductSaleAttr;
import com.wxk.gmall.manage.util.PmsUploadUtil;
import com.wxk.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-28 20:47
 */

@Controller
@CrossOrigin
public class SpuController {



    @Reference
    SpuService spuService;

    //商品列表   小米6 种类
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }




    //添加spu商品列表   小米10
    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }




    //图片处理   小米10的所有图片
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){

        //将图片或者视屏上传到分布式文件系统
        //fdfs？
        //将图片的存储路径（服务器的地址）返回给页面
        //String imgUrl = "https://img14.360buyimg.com/n0/jfs/t1/128784/17/9413/96141/5f32361dE4701ca60/b58ab2d79b859f39.jpg";
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }


    //添加sku显示销售属性列表
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return  pmsProductSaleAttrs;
    }

    //添加sku显示销图片
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return  pmsProductImages;
    }



}
