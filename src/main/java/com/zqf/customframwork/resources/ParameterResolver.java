package com.zqf.customframwork.resources;

import com.zqf.customframwork.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ParameterResolver {

    private Handler handler;

    public ParameterResolver(Handler handler) {
        this.handler = handler;
    }

    public Object[]  resolver(HttpServletRequest request, HttpServletResponse response){
        //组织参数
        Object[] param = new Object[handler.getParamIndexMapping().size()];
        //获取http 传过来的 参数集合
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(Map.Entry<String,String[]> paramEntry : parameterMap.entrySet()){
            String value = StringUtils.join(paramEntry.getValue(), ",");
            if(!handler.getParamIndexMapping().containsKey(paramEntry.getKey())){continue;}
            Integer index = handler.getParamIndexMapping().get(paramEntry.getKey()); //索引值
            param[index] = value;
        }
        int requestIndex = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
        int responseIndex = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
        param[requestIndex] = request;
        param[responseIndex]= response;
        return  param;
    }
}
