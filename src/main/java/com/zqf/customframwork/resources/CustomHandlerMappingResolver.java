package com.zqf.customframwork.resources;

import com.zqf.customframwork.annotations.RequestMapping;
import com.zqf.customframwork.interceptors.Interceptor;
import com.zqf.customframwork.pojo.Handler;
import com.zqf.customframwork.pojo.HandlerChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

public class CustomHandlerMappingResolver{

//    private Map<String,Handler>  handlerMapping = new HashMap<>();
    /**
     * 初始化加载的所有Handler
     */
    private List<Handler>  handlerMapping = new ArrayList<>();
    /**
     * 初始化记载所有的拦截器
     */
    private List<Interceptor> interceptors = new ArrayList<>();

    public List<Handler>  getHandlerMapping(Map<String, Object> singletonObjects) throws Exception {
        String classUrl = "";
        /**
         * 遍历所有的bean，缓存所有 配置了 @RequestMapping 注解的方法。
         */
        for (Object obj : singletonObjects.values()) {
            Class<?> aClass = obj.getClass();
            //如果是拦截器，则缓存拦截器
            if(obj instanceof  Interceptor){
                interceptors.add((Interceptor) obj);
                continue;
            }
            if(aClass.isAnnotationPresent(RequestMapping.class)){
                classUrl = aClass.getAnnotation(RequestMapping.class).value();
            }
            for (Method method : aClass.getMethods()) {
                if(method.isAnnotationPresent(RequestMapping.class)){
                    String methodUrl = method.getAnnotation(RequestMapping.class).value();
                    Handler handler = new Handler(obj,method, Pattern.compile(classUrl+methodUrl));
                    //计算方法的位置信息，即所在形参的下标 HttpServletRequest request, HttpServletResponse response, String name
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        if(parameters[i].getType()== HttpServletRequest.class||parameters[i].getType()== HttpServletResponse.class){
                            //如果是 HttpServletRequest  或者 HttpServletResponse 类型的
                            handler.getParamIndexMapping().put(parameters[i].getType().getSimpleName(),i);
                        }else{
                            handler.getParamIndexMapping().put(parameters[i].getName(),i);
                        }
                    }
                    handlerMapping.add(handler);
                }
            }
        }

        return handlerMapping;
    }


    public HandlerChain getHandlerMapping(String uri){
        for (Handler handler : this.handlerMapping) {
            if(handler.getPattern().matcher(uri).matches()){
                return  new HandlerChain(handler,interceptors);
            }
        }
        return  null;
    }
}
