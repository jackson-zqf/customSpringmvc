package com.zqf.customframwork.bean;

import java.util.Map;
import java.util.Set;

public interface BeanFactory {

    Object  getBean(String beanName);

    Map<String, Object>  initBean(Set<String>  classNames) throws Exception;
}
