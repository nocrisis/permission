package com.rbac.controller;

import com.rbac.common.JsonData;
import com.rbac.param.DeptParam;
import com.rbac.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/sys/dept")
public class SysDeptController {
    @Autowired
    private SysDeptService deptService;
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param) {
        deptService.save(param);
        return JsonData.success();
    }
}
