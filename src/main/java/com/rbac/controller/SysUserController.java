package com.rbac.controller;

import com.rbac.common.JsonData;
import com.rbac.model.SysUser;
import com.rbac.param.UserParam;
import com.rbac.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService userService;
    @RequestMapping("/login")
    public JsonData login(UserParam loginParam) {
        //todo validate
        List<SysUser> users = userService.getUsers(loginParam);
        if (CollectionUtils.isNotEmpty(users)) {
            SysUser sysUser = users.get(0);
            if(loginParam.getPassword().equals(sysUser.getPassword())){
                return JsonData.success();
            }else {
                return JsonData.fail("密码不正确");
            }
        }else{
            return JsonData.fail("该邮箱还没有注册过");
        }
    }

}
