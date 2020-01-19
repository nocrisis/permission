package com.rbac.service;

import com.google.common.collect.Lists;
import com.rbac.dao.SysAclMapper;
import com.rbac.dao.SysRoleAclMapper;
import com.rbac.dao.SysRoleUserMapper;
import com.rbac.model.SysAcl;
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
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUserId();
        return getUserAclList(userId);
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
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
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
        //todo aclId=>userAclId
        return sysAclMapper.getByIdList(userAclIdList);
    }

    public boolean isSuperAdmin() {
        return true;
    }


}
