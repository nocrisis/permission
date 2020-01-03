package com.rbac.service;

import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysAclModuleMapper;
import com.rbac.model.SysAclModule;
import com.rbac.param.AclModuleParam;
import com.rbac.util.BeanValidator;
import com.rbac.util.IPUtil;
import com.rbac.util.RequestHolder;

import javax.annotation.Resource;
import java.util.Date;

public class SysAclModuleService {
    @Resource
    SysAclModuleMapper aclModuleMapper;

    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (param.getParentId() == null) {
            param.setParentId(0);
        }
        if (checkExited(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule sysAclModule = SysAclModule.builder().name(param.getName())
                .parentId(param.getParentId()).seq(param.getSeq()).memo(param.getMemo()).build();
        sysAclModule.setOperatorTime(new Date());
        sysAclModule.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperator(RequestHolder.getCurrentUsername());
        aclModuleMapper.insertSelective(sysAclModule);
    }
    public void update(AclModuleParam aclModuleParam) {

    }
    public void updateWithChild(SysAclModule before,SysAclModule after) {

    }

    public boolean checkExited(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return aclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();//权限模块level
    }
}
