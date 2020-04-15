package com.rbac.service;

import com.google.common.collect.Lists;
import com.rbac.dao.SysAclMapper;
import com.rbac.dao.SysRoleAclMapper;
import com.rbac.dao.SysRoleMapper;
import com.rbac.dao.SysRoleUserMapper;
import com.rbac.model.SysAcl;
import com.rbac.model.SysRole;
import com.rbac.util.RequestHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysCoreService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    public List<SysAcl> getCurrentUserAclList(int roleId) {
        int userId = RequestHolder.getCurrentUserId();
        return getUserAclList(userId,roleId);
    }
    //角色->权限
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }
    //用户->角色->权限
    public List<SysAcl> getUserAclList(int userId,int roleId) {
        if (isSuperAdmin(roleId)) {
            return sysAclMapper.getAll();
        }
        List<Integer> userRoleList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if(CollectionUtils.isNotEmpty(userRoleList)){
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleList);
        if(CollectionUtils.isNotEmpty(userAclIdList)){
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

//todo 这里是我瞎编的，默认role type 是管理员的就是超级管理员，实际上超级管理员最好与角色解耦，与用户相关
    public boolean isSuperAdmin(int roleId) {
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
        if (role.getType() == 1) {
            return true;
        }else {
            return false;
        }
    }


}
