package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.JsonData;
import com.rbac.model.SysUser;
import com.rbac.param.UserParam;
import com.rbac.service.SysUserService;
import com.rbac.util.BeanValidator;
import com.rbac.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    //没有@RequestBody不能接受post在body中的参数（null）
    public JsonData login(@RequestBody String payload) {
        log.info("login:{}" , payload);
        UserParam loginParam = JSON.parseObject(payload, UserParam.class);
        BeanValidator.check(loginParam);
        List<SysUser> users = userService.getUsers(loginParam);
        if (CollectionUtils.isNotEmpty(users)) {
            SysUser sysUser = users.get(0);
            //todo md5 pwd
            if (loginParam.getPassword().equals(sysUser.getPassword())) {
                return JsonData.success(JWTUtil.createToken(sysUser.getUsername()));
            } else {
                return JsonData.fail("密码不正确");
            }
        } else {
            return JsonData.fail("该邮箱还没有注册过");
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public JsonData register(@RequestBody String payload) {
        log.info("register:{}", payload);
        SysUser sysUser = JSON.parseObject(payload, SysUser.class);
        BeanValidator.check(sysUser);
        UserParam userParam = new UserParam();
        userParam.setMail(sysUser.getMail());
        List<SysUser> users = userService.getUsers(userParam);
        if (CollectionUtils.isEmpty(users)) {
            boolean res = userService.insert(sysUser);
            if (res) {
                return JsonData.success();
            } else {
                return JsonData.fail("系统错误");
            }
        } else {
            return JsonData.fail("该邮箱已经注册过");
        }
    }
    //todo code 401 token失效

}
