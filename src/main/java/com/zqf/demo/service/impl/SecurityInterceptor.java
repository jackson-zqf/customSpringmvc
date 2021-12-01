package com.zqf.demo.service.impl;

import com.zqf.customframwork.annotations.Compont;
import com.zqf.customframwork.annotations.Security;
import com.zqf.customframwork.interceptors.Interceptor;
import com.zqf.customframwork.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> authorities = new ArrayList<>();
        String username = request.getParameter("username");
        Security annotation1 = handler.getObj().getClass().getAnnotation(Security.class);
        String[] value1 = annotation1.value();
        Security annotation = handler.getMethod().getAnnotation(Security.class);
        if (annotation != null) {
            String[] value2 = annotation.value();
            authorities = intersect(value1, value2);
        }
        if (authorities.contains(username)) {
            return true;
        }
        response.getWriter().write("权限校验失败，不允许该用户访问");
        return false;
    }


    private List<String> intersect(String[] arr1, String[] arr2) {
        if (arr1 == null) {
            return Arrays.asList(arr2);
        }
        if (arr2 == null) {
            return Arrays.asList(arr1);
        }
        List<String> strings = Arrays.asList(arr1);
        List<String> strings1 = Arrays.asList(arr2);
        strings = new ArrayList<>(strings);
        strings1 = new ArrayList<>(strings1);
        strings.retainAll(strings1);
        return strings;
    }


}
