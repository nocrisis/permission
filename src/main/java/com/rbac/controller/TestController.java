package com.rbac.controller;

import com.rbac.common.JsonData;
import com.rbac.common.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController
{
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new RuntimeException("text Exception");
//        throw new PermissionException("text Exception");
//        return JsonData.success("hello.permission");
    }
}
