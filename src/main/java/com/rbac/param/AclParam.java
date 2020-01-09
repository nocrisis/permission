package com.rbac.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AclParam {
    @NotNull(groups = Update.class,message = "权限点Id不能为空")
    private Integer id;
    @Length(max = 15, min = 2, message = "权限模块名称在2-15个字之间")
    @NotNull(message = "权限模块名称不可以为空")
    private String name;
    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;
    @Length(max = 100, min = 6, message = "权限点URL长度需要在6-100个字之间")
    private String url;
    @NotNull(message = "必须指定权限点的类型")
    @Min(value = 0, message = "权限点类型不合法")
    @Max(value = 2, message = "权限点类型不合法")
    private Integer type;
    @NotNull(message = "必须指定权限点的状态")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 2, message = "权限点状态不合法")
    private Integer status;
    @NotNull(message = "权限点展示顺序不可以为空")
    private Integer seq;
    @Length(max = 150, message = "备注的长度需要在150个字之间")
    private String memo;
}
