package com.rbac.dto;

import com.google.common.collect.Lists;
import com.rbac.model.SysAclModule;
import com.rbac.param.AclModuleParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class AclModuleLevelDTO extends SysAclModule{

    public List<AclModuleLevelDTO> aclModuleList = Lists.newArrayList();

    public List<AclDTO> aclDTOList = Lists.newArrayList();

    public static AclModuleLevelDTO adapt(SysAclModule aclModule) {
        AclModuleLevelDTO aclModuleLevelDTO = new AclModuleLevelDTO();
        BeanUtils.copyProperties(aclModule, aclModuleLevelDTO);
        return aclModuleLevelDTO;
    }
}
