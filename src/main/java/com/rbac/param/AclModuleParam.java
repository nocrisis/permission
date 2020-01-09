package com.rbac.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class  AclModuleParam {
    private Integer id;
    @Length(max = 15, min = 2, message = "权限模块名称在2-15个字之间")
    @NotNull(message = "权限模块名称不可以为空")
    private String name;
    private Integer parentId;
    @Min(value = 0, message = "权限模块状态不合法")
    @Max(value = 2, message = "权限模块状态不合法")
    @NotNull(message = "权限模块状态不可以为空")
    private Integer status;
    @NotNull(message = "权限模块展示顺序不可以为空")
    private Integer seq;
    @Length(max = 150, message = "备注的长度需要在150个字之间")
    private String memo;
}
