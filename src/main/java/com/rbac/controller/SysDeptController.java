package com.rbac.controller;

import com.rbac.common.bean.JsonData;
import com.rbac.dto.DeptLevelDTO;
import com.rbac.param.DeptParam;
import com.rbac.service.SysDeptService;
import com.rbac.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/sys/dept")
public class SysDeptController {
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page() {
        ModelAndView mv= new ModelAndView("dept");
        return  mv;
    }
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(@RequestBody DeptParam param) {
        deptService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDTO> deptLevelDTOList = sysTreeService.deptTree();
        return JsonData.success(deptLevelDTOList);
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(@RequestBody DeptParam param) {
        deptService.update(param);
        return JsonData.success();
    }

    @DeleteMapping
    @ResponseBody
    public JsonData deleteDept(@PathVariable("id") int id){
        log.info("delete dept id {}",id);
        //todo
        return JsonData.success();
    }

}
