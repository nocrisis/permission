package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.ApplicationContextHelper;
import com.rbac.common.bean.JsonData;
import com.rbac.common.exception.ParamException;
import com.rbac.common.exception.PermissionException;
import com.rbac.dao.SysAclModuleMapper;
import com.rbac.dto.AclModuleLevelDTO;
import com.rbac.model.SysAclModule;
import com.rbac.param.ValidateTestVO;
import com.rbac.service.SysTreeService;
import com.rbac.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
//        throw new RuntimeException("text Exception");
        throw new PermissionException("text Exception");
//        return JsonData.success("hello.permission");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(ValidateTestVO testVO) throws ParamException {
        log.info("validate test");
/*        try {
            Map<String, String> map = BeanValidator.validateObject(testVO);
            if (MapUtils.isNotEmpty(map)) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    log.info("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }*/
        BeanValidator.check(testVO);
        return JsonData.success("test Validate");
    }

    @RequestMapping("/appcontext.json")
    @ResponseBody
    public JsonData popByApp() throws ParamException {
        log.info("popByApp test");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JSON.toJSONString(module));
        return JsonData.success("test ApplicationContextHelper");
    }

    @RequestMapping("/testDfs")
    public String testDfs() {
        List<AclModuleLevelDTO> aclModuleList = sysTreeService.moduleTree();
        String name = bfs(13, aclModuleList);
        return name;
    }

    private String bfs(int moduleId, List<AclModuleLevelDTO> tree) {
        String name = null;
        if (tree != null) {
            for (AclModuleLevelDTO module : tree) {
                log.info("id:{}",module.getId());
                if (module.getId() == moduleId) {
                    name = module.getName();
                    return name;
                }
                if (module.getAclModuleList() != null) {
                    name = bfs(moduleId, module.getAclModuleList());
                }
            }
        } else {
            return name;
        }
        return name;
    }

}
