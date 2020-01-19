package com.rbac.dto;

import com.rbac.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ToString
public class AclDTO extends SysAcl {
    //是否默认选中
    private boolean checked = false;
    //是否有权限操作
    private boolean hasAcl = false;

    public static AclDTO adapt(SysAcl sysAcl) {
        AclDTO aclDTO = new AclDTO();
        BeanUtils.copyProperties(sysAcl, aclDTO);
        return aclDTO;
    }
}
