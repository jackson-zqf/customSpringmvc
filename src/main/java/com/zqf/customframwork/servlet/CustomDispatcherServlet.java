package com.zqf.customframwork.servlet;

import com.zqf.customframwork.bean.BeanFactory;
import com.zqf.customframwork.bean.DefaultBeanFactory;
import com.zqf.customframwork.pojo.Handler;
import com.zqf.customframwork.pojo.HandlerChain;
import com.zqf.customframwork.resources.CustomHandlerMappingResolver;
import com.zqf.customframwork.resources.CustomPackageResolver;
import com.zqf.customframwork.resources.CustomResourceResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CustomDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private CustomHandlerMappingResolver customHandlerMappingResolver;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=utf-8");
        //1.获取请求 uri  /demo/query
        String uri = req.getRequestURI();

        //2.根据uri 获取HandlerChain 执行链对象(handler + 拦截器)
        HandlerChain handlerChain = customHandlerMappingResolver.getHandlerMapping(uri);
        if(handlerChain == null){
            resp.getWriter().write("没有找到该路径的请求："+uri);
            return;
        }
        //3.执行方法
        try {
            handlerChain.invoke(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String contextLoadConfig = config.getInitParameter("contextLoadConfig");
        CustomResourceResolver customResourceResolver = new CustomResourceResolver(properties);
        try {
            //1.加载配置文件 springmvc.properties
            customResourceResolver.loadingConfig(contextLoadConfig);

            //2.扫描包，获取所有类的全限定名
            CustomPackageResolver customPackageResolver = new CustomPackageResolver();
            Set<String> classNames = customPackageResolver.getFullyQualifiedName((String) properties.get("scanPackage"));

            //3.扫描注解，初始化ioc容器, 4.依赖注入
            BeanFactory beanFactory = new DefaultBeanFactory();
            Map<String, Object> stringObjectMap = beanFactory.initBean(classNames);
            //5.创建 HandlerMapping 处理器映射器
            customHandlerMappingResolver = new CustomHandlerMappingResolver();
            customHandlerMappingResolver.getHandlerMapping(stringObjectMap);

            System.out.println("springmvc 初始化完成。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
