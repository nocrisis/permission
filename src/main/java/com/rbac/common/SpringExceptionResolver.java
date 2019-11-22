package com.rbac.common;

import com.rbac.common.exception.ParamException;
import com.rbac.common.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
//引入jsp-api
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "Systerm error";

        //要求项目所有请求json数据以.json结尾
        if (url.endsWith(".json")) {
            if (e instanceof PermissionException || e instanceof ParamException) {
                JsonData result = JsonData.fail(e.getMessage());
                //servlet.xml    <bean id="jsonView" class="com.alibaba.fastjson.support.spring.FastJsonJsonView"/>
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("unknown json exception , url:" + url, e);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
            //要求项目所有请求page页面以.page结尾
        } else if (url.endsWith(".page")) {
            log.error("unknown page exception , url:" + url, e);

            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        } else {
            log.error("unknown exception , url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());

        }
        return mv;
    }


}
