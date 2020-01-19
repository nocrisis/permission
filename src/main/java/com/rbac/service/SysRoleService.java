package com.rbac.service;

import com.google.common.base.Preconditions;
import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysRoleMapper;
import com.rbac.model.SysRole;
import com.rbac.param.RoleParam;
import com.rbac.param.Update;
import com.rbac.util.BeanValidator;
import com.rbac.util.IPUtil;
import com.rbac.util.RequestHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    public void save(RoleParam param) {
        BeanValidator.check(param, Default.class);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("存在相同的角色名称");
        }
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus())
                .type(param.getType()).memo(param.getMemo()).build();
        role.setOperator(RequestHolder.getCurrentUsername());
        role.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        role.setOperatorTime(new Date());
        sysRoleMapper.insertSelective(role);
    }

    public void update(RoleParam param) {
        BeanValidator.check(param, Update.class);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus())
                .type(param.getType()).memo(param.getMemo()).build();
        after.setOperator(RequestHolder.getCurrentUsername());
        after.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        after.setOperatorTime(new Date());
        sysRoleMapper.updateByPrimaryKey(after);
    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countNameById(name, id) == 1;
    }

    public List<SysRole> listAll() {
        return sysRoleMapper.getAll();
    }

    public void delete(Integer id) {
        SysRole before = sysRoleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "待删除的角色不存在");
        before.setStatus(1);
        sysRoleMapper.updateByPrimaryKeySelective(before);
    }

}
