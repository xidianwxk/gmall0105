package com.wxk.gmall.manage.mapper;

import com.wxk.gmall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wxk
 * @creat 2020-10-27 21:18
 */

public interface PmsBaseAttrInfoMapper extends Mapper <PmsBaseAttrInfo>{
    List<PmsBaseAttrInfo> selectAttrValueListByValueId(@Param("valueIdStr") String valueIdStr);
}
