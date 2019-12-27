package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.JsonData;
import com.rbac.common.exception.ParamException;
import com.rbac.model.SysUser;
import com.rbac.param.Update;
import com.rbac.param.UserParam;
import com.rbac.service.SysDeptService;
import com.rbac.service.SysUserService;
import com.rbac.util.BeanValidator;
import com.rbac.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysDeptService deptService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    //没有@RequestBody不能接受post在body中的参数（null）
    public JsonData login(@RequestBody String payload) {
        log.info("login:{}", payload);
        UserParam loginParam = JSON.parseObject(payload, UserParam.class);
        Map<String, String> map = BeanValidator.validate(loginParam, Default.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        List<SysUser> users = userService.getUsers(loginParam);
        if (CollectionUtils.isNotEmpty(users)) {
            SysUser sysUser = users.get(0);
            //todo md5 pwd
            if (loginParam.getPassword().equals(sysUser.getPassword())) {
                String dept = deptService.getDeptNameById(sysUser.getDeptId());
                return JsonData.success(JWTUtil.createToken(sysUser.getId(), sysUser.getUsername(),dept));
            } else {
                return JsonData.fail("密码不正确");
            }
        } else {
            return JsonData.fail("该邮箱或手机号还没有注册过");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public JsonData register(@RequestBody String payload) {
        log.info("register:{}", payload);
        UserParam userParam = JSON.parseObject(payload, UserParam.class);
        userParam.setStatus(1);
        Map<String, String> map = BeanValidator.validate(userParam, Update.class,Default.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        List<SysUser> users = userService.getUsers(userParam);
        if (CollectionUtils.isEmpty(users)) {
            SysUser insert = new SysUser();
            BeanUtils.copyProperties(userParam, insert);
            boolean res = userService.insert(insert);
            if (res) {
                return JsonData.success();
            } else {
                return JsonData.fail("系统错误");
            }
        } else {
            return JsonData.fail("该邮箱或电话已经注册过");
        }
    }
    //todo code 401 token失效

}
