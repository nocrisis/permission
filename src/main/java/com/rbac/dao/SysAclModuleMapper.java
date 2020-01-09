package com.rbac.dao;

import com.rbac.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);
    int batchDelete(List<Integer> id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    //--排除自己本身的存在外是否有重复,这里id不能为int,@Param会爆空指针
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysAclModule> getAllModule();
    List<SysAclModule> getChildModuleListByLevel(@Param("level") String level);
    void batchUpdateLevel(@Param("aclModuleList") List<SysAclModule> moduleList);

}