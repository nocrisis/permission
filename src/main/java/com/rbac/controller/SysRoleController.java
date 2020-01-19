package com.rbac.controller;

import com.rbac.common.bean.JsonData;
import com.rbac.model.SysRole;
import com.rbac.param.RoleParam;
import com.rbac.service.SysRoleAclService;
import com.rbac.service.SysRoleService;
import com.rbac.service.SysTreeService;
import com.rbac.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleAclService sysRoleAclService;

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveRole(@RequestBody RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonData list() {
        List<SysRole> roles = sysRoleService.listAll();
        return JsonData.success(roles);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateRole(@RequestBody RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public JsonData deleteRole(@PathVariable("id") int id) {
        log.info("delete role id {}", id);
        sysRoleService.delete(id);
        return JsonData.success();
    }

    @RequestMapping("/roleTree")
    @ResponseBody
    public JsonData roleData(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    @RequestMapping("/changeAcls")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }

}
