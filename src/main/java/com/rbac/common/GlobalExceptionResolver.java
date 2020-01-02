package com.rbac.common;

import com.rbac.common.bean.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e) {
        // 打印异常堆栈信息
        log.error(e.toString(), e);
        return JsonData.fail(e.getMessage());
    }
}
