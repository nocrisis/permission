package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.bean.JsonData;
import com.rbac.common.bean.PageResult;
import com.rbac.common.exception.ParamException;
import com.rbac.model.SysUser;
import com.rbac.param.Insert;
import com.rbac.param.ListUserParam;
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
import org.springframework.web.bind.annotation.*;

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
                return JsonData.success(JWTUtil.createToken(sysUser.getId(), sysUser.getUsername(), dept));
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
        Map<String, String> map = BeanValidator.validate(userParam, Insert.class, Default.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        boolean isExited = userService.checkNumberExits(userParam.getTelephone(), userParam.getMail());
        if (!isExited) {
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
    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonData list(@RequestBody String payload) {
        log.info("user list:{}", payload);
        ListUserParam listUserParam = JSON.parseObject(payload, ListUserParam.class);
        PageResult<SysUser> sysUsers = userService.listUsers(listUserParam);
        return JsonData.success(sysUsers);
    }

    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonData get(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            throw new ParamException("userId不能为空");
        }
        SysUser sysUser = userService.getUserByUserId(userId);
        return JsonData.success(sysUser);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonData get(@RequestBody String payload) {
        log.info("user edit:{}", payload);
        UserParam param = JSON.parseObject(payload, UserParam.class);
        Map<String, String> map = BeanValidator.validate(param, Default.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        boolean res = userService.update(param);
        if (res) {
            return JsonData.success();
        } else {
            return JsonData.fail("更新失败");
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public JsonData delete(@PathVariable("id") int id) {
        log.info("delete user id {}", id);
        boolean res = userService.delete(id);
        if (res) {
            return JsonData.success();
        } else {
            return JsonData.fail("删除失败");
        }
    }

}
