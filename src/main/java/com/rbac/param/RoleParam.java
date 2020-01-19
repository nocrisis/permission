package com.rbac.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RoleParam {
    @NotNull(groups = Update.class,message = "角色Id不能为空")
    private Integer id;
    @Length(max = 15, min = 2, message = "角色名称在2-15个字之间")
    @NotNull(message = "角色名称不可以为空")
    private String name;
    @NotNull(message = "必须指定角色的类型")
    @Min(value = 0, message = "角色类型不合法")
    @Max(value = 2, message = "角色类型不合法")
    private Integer type;
    @NotNull(message = "必须指定角色的状态")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 2, message = "角色状态不合法")
    private Integer status;
    @Length(max = 150, message = "备注的长度需要在150个字之间")
    private String memo;
}
