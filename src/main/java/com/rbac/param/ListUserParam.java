package com.rbac.param;

import com.rbac.common.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;

public class ListUserParam extends PageQuery {
    @Getter
    @Setter
    private int deptId;
}
