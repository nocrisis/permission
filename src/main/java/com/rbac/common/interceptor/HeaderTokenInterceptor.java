package com.rbac.common.interceptor;

import com.rbac.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HeaderTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String headerToken = httpServletRequest.getHeader("Authorization");
//        if (!httpServletRequest.getRequestURI().contains("login")) {
            if (headerToken == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            try {
                boolean verify = JWTUtil.verify(headerToken);
                if (verify) {
                    return true;
                }
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {

            }
//        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
