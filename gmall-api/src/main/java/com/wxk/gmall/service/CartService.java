package com.wxk.gmall.service;

import com.wxk.gmall.bean.OmsCartItem;

import java.util.List;


/**
 * @author wxk
 * @creat 2020-11-24 14:25
 */

public interface CartService {

    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);


}
