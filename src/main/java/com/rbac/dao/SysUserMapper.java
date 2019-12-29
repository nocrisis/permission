package com.rbac.dao;

import com.rbac.model.SysUser;
import com.rbac.param.ListUserParam;
import com.rbac.param.UserParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    //javabean查询时不能对bean用@Param,param是针对单个字段，否则找不到对应字段
    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(@Param("id") Integer id);

    List<SysUser> selectByModel(UserParam param);

    List<SysUser> selectByPhoneOrMail(UserParam param);

    List<SysUser> listUsersByDeptId(@Param("param") ListUserParam param);

    int countUsersByDeptId(@Param("deptId") Integer deptId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);


}