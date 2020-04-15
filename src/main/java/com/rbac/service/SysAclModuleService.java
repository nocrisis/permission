package com.rbac.service;

import com.google.common.base.Preconditions;
import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysAclModuleMapper;
import com.rbac.handler.LevelHandler;
import com.rbac.model.SysAclModule;
import com.rbac.param.AclModuleParam;
import com.rbac.util.BeanValidator;
import com.rbac.util.IPUtil;
import com.rbac.util.RequestHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysAclModuleService {
    @Resource
    SysAclModuleMapper aclModuleMapper;

    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (param.getParentId() == null) {
            param.setParentId(0);
        }
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule sysAclModule = SysAclModule.builder().name(param.getName()).status(param.getStatus())
                .parentId(param.getParentId()).seq(param.getSeq()).memo(param.getMemo()).build();
        sysAclModule.setLevel(LevelHandler.calculateLevel(getLevelById(param.getParentId()), param.getParentId()));
        sysAclModule.setOperatorTime(new Date());
        sysAclModule.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperator(RequestHolder.getCurrentUsername());
        aclModuleMapper.insertSelective(sysAclModule);
    }

    public void update(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before = aclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");
        SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).status(param.getStatus()).memo(param.getMemo()).build();
        after.setLevel(LevelHandler.calculateLevel(getLevelById(param.getParentId()), param.getParentId()));
        after.setOperatorTime(new Date());
        after.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        after.setOperator(RequestHolder.getCurrentUsername());
        updateWithChild(before, after);
    }

    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        //如果级别发生了变化
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysAclModule> aclMoudules = aclModuleMapper.getChildModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclMoudules)) {
                for (SysAclModule dept : aclMoudules) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        //含有待更改级别的旧前缀更换成新前缀前缀
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                aclModuleMapper.batchUpdateLevel(aclMoudules);
            }
        }
        aclModuleMapper.updateByPrimaryKey(after);
    }

    public boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return aclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevelById(Integer aclModuleId) {
        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();//权限模块level
    }

    @Transactional
    public void delete(Integer id) {
        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(id);
        String childLevel = LevelHandler.calculateLevel(aclModule.getLevel(), id);
        List<SysAclModule> childs = aclModuleMapper.getChildModuleListByLevel(childLevel);
        List<Integer> ids = childs.stream().collect(Collectors.mapping(SysAclModule::getId, Collectors.toList()));
        if (CollectionUtils.isNotEmpty(childs)) {
            aclModuleMapper.batchDelete(ids);
        }
        aclModuleMapper.deleteByPrimaryKey(id);
    }
}
