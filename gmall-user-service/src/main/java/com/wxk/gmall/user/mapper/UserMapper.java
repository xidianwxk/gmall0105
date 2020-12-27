package com.wxk.gmall.user.mapper;

import com.wxk.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-12 21:28
 */

public interface UserMapper extends Mapper<UmsMember> {

    List<UmsMember> selectAllUser();
}
