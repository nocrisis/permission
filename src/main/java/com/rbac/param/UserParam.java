package com.rbac.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UserParam {
    private Integer id;
    @NotBlank(message = "用户名不可以为空",groups = Insert.class)
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;
    @NotBlank(message = "手机不可以为空",groups = Insert.class)
    private String telephone;
    @NotBlank(message = "邮箱不可以为空",groups = Insert.class)
    private String mail;
    @NotBlank
    @Length(min = 6, max = 20, message = "密码长度需要在6-20个字以内")
    private String password;
    @NotNull(message = "必须提供用户所在的部门",groups = Insert.class)
    private Integer deptId;
    /*@NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")*/
    private Integer status;
    @Length(message = "备注长度需要在200个字以内")
    private String memo;

}
