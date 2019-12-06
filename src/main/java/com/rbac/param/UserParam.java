package com.rbac.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UserParam {
    private Integer userId;
    @JSONField(name = "name")
    private String username;
    private String telephone;
    @JSONField(name = "email")
    @NotBlank
    private String mail;
    @NotBlank
    private String password;

}
