package com.zqf.demo.service.impl;

import com.zqf.customframwork.annotations.Compont;
import com.zqf.customframwork.annotations.Security;
import com.zqf.customframwork.interceptors.Interceptor;
import com.zqf.customframwork.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限校验拦截器
 */
@Compont
public class SecurityInterceptor implements Interceptor {

    @Override
    public boolean doIntercept(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        /**
         * 获取用户名，对用户名进行拦截
         */
        String username = request.getParameter("username");
        Security annotation = handler.getMethod().getAnnotation(Security.class);
        if(annotation!=null){
            String[] value = annotation.value();
            for (String s : value) {
                if(s.equals(username)){
                    return  true;
                }
            }
        }
        response.getWriter().write("权限校验失败，不允许该用户访问");
        return false;
    }
}
