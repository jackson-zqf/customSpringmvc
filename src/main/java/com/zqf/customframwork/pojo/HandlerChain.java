package com.zqf.customframwork.pojo;

import com.zqf.customframwork.interceptors.Interceptor;
import com.zqf.customframwork.resources.ParameterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class HandlerChain {

    private Handler handler;

    private List<Interceptor> interceptors;

    public HandlerChain(Handler handler, List<Interceptor> interceptors) {
        this.handler = handler;
        this.interceptors = interceptors;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }


    public void invoke(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1.判断是否有拦截器,如果是则先执行拦截器
        for (Interceptor interceptor : interceptors) {
            boolean b = interceptor.doIntercept(req, resp,handler);
            if(!b){
                return;
            }
        }
        //2.组织参数
        ParameterResolver parameterResolver = new ParameterResolver(handler);
        Object[] param = parameterResolver.resolver(req,resp);
        //3.反射调用执行目标方法
        handler.getMethod().invoke(handler.getObj(),param);
    }
}
