package com.rbac.dao;

import com.rbac.model.SysUser;
import com.rbac.param.UserParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(@Param("id") Integer id);

    List<SysUser> selectByModel(@Param("param") UserParam param);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}