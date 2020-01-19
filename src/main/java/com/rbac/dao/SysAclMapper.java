package com.rbac.dao;

import com.rbac.model.SysAcl;
import com.rbac.param.ListAclParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int updateStatusByPrimaryKey(@Param("id")int id, @Param("status") int status);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    List<SysAcl> getPageByAclModuleId(@Param("param") ListAclParam param);

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("ids") List<Integer> ids);

    int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    int countByNameAndAclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("aclName") String aclName, @Param("aclId") Integer aclId);

}