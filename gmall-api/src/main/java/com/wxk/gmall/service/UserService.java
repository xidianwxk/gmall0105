package com.wxk.gmall.service;

import com.wxk.gmall.bean.UmsMember;
import com.wxk.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-12 21:26
 */

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token,String memberId);

    UmsMember checkOauthUser(UmsMember umsCheck);

    UmsMember addOauthUser(UmsMember umsMember);

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);
}
