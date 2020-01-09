package com.rbac.param;

import com.rbac.common.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;

public class ListAclParam extends PageQuery {
    @Getter
    @Setter
    private int aclModuleId;
}