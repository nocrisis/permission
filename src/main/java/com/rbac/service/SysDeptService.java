package com.rbac.service;

import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysDeptMapper;
import com.rbac.handler.LevelHandler;
import com.rbac.model.SysDept;
import com.rbac.param.DeptParam;
import com.rbac.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    public void save(DeptParam param) {
        BeanValidator.check(param);
        if (checkExits(param.getParentId(), param.getName(),param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept=SysDept.builder().name(param.getName()).parentId(param.getId())
                .seq(param.getSeq()).memo(param.getMemo()).build();
        dept.setLevel(LevelHandler.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator("system");
        dept.setOperatorIp("127.0.0.1");
        dept.setOperatorTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }

    private boolean checkExits(Integer parentId, String deptName, Integer deptId) {
        //todo
        return true;
    }

    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();//部门level
    }
}
