package com.rbac.service;

import com.google.common.base.Preconditions;
import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysDeptMapper;
import com.rbac.handler.LevelHandler;
import com.rbac.model.SysDept;
import com.rbac.param.DeptParam;
import com.rbac.util.BeanValidator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    public void save(DeptParam param) {
        BeanValidator.check(param);
        if (checkExits(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).memo(param.getMemo()).build();
        dept.setLevel(LevelHandler.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator("system");
        dept.setOperatorIp("127.0.0.1");
        dept.setOperatorTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }

    public void update(DeptParam param) {
        BeanValidator.check(param);
        if (checkExits(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if (checkExits(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).memo(param.getMemo()).build();
        after.setLevel(LevelHandler.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator("system-update");
        after.setOperatorIp("127.0.0.1");
        after.setOperatorTime(new Date());
        updateWithChild(before, after);
    }

    @Transactional
    public void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        //如果级别发生了变化
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        //含有待更改级别的旧前缀更换成新前缀前缀
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);

            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    private boolean checkExits(Integer parentId, String deptName, Integer deptId) {
        //parentId不能为空
        int exitsCount = sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId);
        return exitsCount > 0;
    }

    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();//部门level
    }
}
