package com.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.rbac.common.ApplicationContextHelper;
import com.rbac.common.JsonData;
import com.rbac.common.exception.ParamException;
import com.rbac.common.exception.PermissionException;
import com.rbac.dao.SysAclModuleMapper;
import com.rbac.model.SysAclModule;
import com.rbac.param.ValidateTestVO;
import com.rbac.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController
{
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
}
