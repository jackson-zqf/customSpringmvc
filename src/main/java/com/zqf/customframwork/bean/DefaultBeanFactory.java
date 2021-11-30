package com.zqf.customframwork.bean;

import com.zqf.customframwork.annotations.Autowired;
import com.zqf.customframwork.annotations.Compont;
import com.zqf.customframwork.annotations.Controller;
import com.zqf.customframwork.annotations.Service;
import com.zqf.customframwork.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultBeanFactory  implements BeanFactory {

    protected Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getBean(String beanName) {
        return  singletonObjects.get(beanName);
    }

    @Override
    public Map<String,Object> initBean(Set<String> classNames) throws Exception {
        doCreateBean(classNames);
        doAutowired();
        return singletonObjects;
    }

    /**
     * 进行实例的注入
     * @throws IllegalAccessException
     */
    private void doAutowired() throws IllegalAccessException {
        for (Object obj : singletonObjects.values()) {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    String id = StringUtils.isEmpty(annotation.value()) ? field.getName() : annotation.value();
                    Object o = singletonObjects.get(id);
                    field.setAccessible(true);
                    field.set(obj, o);
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 创建bean
     * @param classNames
     * @throws Exception
     */
    private void doCreateBean(Set<String> classNames) throws Exception {
        String beanName;
        //遍历所有的全限定名
        for (String className : classNames) {
            Class<?> aClass = Class.forName(className);
            Object obj = null;
            String annotationValue = null;
            if (aClass.isAnnotationPresent(Controller.class)) {
                Controller annotation = aClass.getAnnotation(Controller.class);
                annotationValue = annotation.value();
                obj = aClass.newInstance();
            } else if (aClass.isAnnotationPresent(Service.class)) {
                Service annotation = aClass.getAnnotation(Service.class);
                annotationValue = annotation.value();
                obj = aClass.newInstance();
            }else if(aClass.isAnnotationPresent(Compont.class)){
                Compont annotation = aClass.getAnnotation(Compont.class);
                annotationValue = annotation.value();
                obj = aClass.newInstance();
            }
            else {
                continue;
            }
            beanName = StringUtils.isEmpty(annotationValue) ? toLowCase(aClass.getSimpleName()) : annotationValue;
            singletonObjects.put(beanName, obj);

            if (aClass.getSuperclass().isInterface()) {
                //如果实现了接口的话，则保存接口名的映射关系
                Class<?>[] interfaces = aClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    singletonObjects.put(anInterface.getName(), obj);
                }
            }
        }
    }


    //首字母大写
    private String toLowCase(String beanName) {
        char[] chars = beanName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
