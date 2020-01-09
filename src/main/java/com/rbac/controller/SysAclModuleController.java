package com.rbac.controller;

import com.rbac.common.bean.JsonData;
import com.rbac.dto.AclModuleLevelDTO;
import com.rbac.dto.AclModuleLevelDTO;
import com.rbac.param.AclModuleParam;
import com.rbac.service.SysAclModuleService;
import com.rbac.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {
    @Autowired
    private SysAclModuleService aclModuleService;
    @Autowired
    private SysTreeService treeService;

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveAclModule(@RequestBody AclModuleParam param) {
        aclModuleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/tree")
    @ResponseBody
    public JsonData tree() {
        List<AclModuleLevelDTO> moduleLevelDTOList = treeService.moduleTree();
        return JsonData.success(moduleLevelDTOList);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateAclModule(@RequestBody AclModuleParam param) {
        aclModuleService.update(param);
        return JsonData.success();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public JsonData deleteAclModule(@PathVariable("id") int id) {
        log.info("delete aclModule id {}", id);
        aclModuleService.delete(id);
        return JsonData.success();
    }

}
