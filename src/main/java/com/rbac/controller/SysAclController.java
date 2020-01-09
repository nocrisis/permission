package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.bean.JsonData;
import com.rbac.common.bean.PageResult;
import com.rbac.common.exception.ParamException;
import com.rbac.model.SysAcl;
import com.rbac.param.AclParam;
import com.rbac.param.ListAclParam;
import com.rbac.param.Update;
import com.rbac.service.SysAclService;
import com.rbac.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService aclService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonData list(@RequestBody String payload) {
        log.info("acl list:{}", payload);
        ListAclParam listAclParam = JSON.parseObject(payload, ListAclParam.class);
        PageResult<SysAcl> sysAclList = aclService.getPageListByAclModuleId(listAclParam);
        return JsonData.success(sysAclList);
    }

    @RequestMapping(value = "/edit/{aclId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonData get(@PathVariable("aclId") Integer aclId) {
        if (aclId == null) {
            throw new ParamException("aclId不能为空");
        }
        SysAcl sysAcl = aclService.getAclByAclId(aclId);
        return JsonData.success(sysAcl);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonData update(@RequestBody String payload) {
        log.info("acl edit:{}", payload);
        AclParam param = JSON.parseObject(payload, AclParam.class);
        Map<String, String> map = BeanValidator.validate(param, Update.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        boolean res = aclService.update(param);
        if (res) {
            return JsonData.success();
        } else {
            return JsonData.fail("更新失败");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonData insert(@RequestBody String payload) {
        log.info("acl add:{}", payload);
        AclParam param = JSON.parseObject(payload, AclParam.class);
        Map<String, String> map = BeanValidator.validate(param, Default.class);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
        boolean res = aclService.save(param);
        if (res) {
            return JsonData.success();
        } else {
            return JsonData.fail("保存权限点失败");
        }
    }


    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public JsonData delete(@PathVariable("id") int id) {
        log.info("delete acl id {}", id);
        boolean res = aclService.delete(id);
        if (res) {
            return JsonData.success();
        } else {
            return JsonData.fail("删除失败");
        }
    }
}
