package com.rbac.dto;

import com.google.common.collect.Lists;
import com.rbac.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class DeptLevelDTO extends SysDept {
    //包含该部门以下的子部门
    private List<DeptLevelDTO> deptList = Lists.newArrayList();

    public static DeptLevelDTO adapt(SysDept dept) {
        DeptLevelDTO dto = new DeptLevelDTO();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
