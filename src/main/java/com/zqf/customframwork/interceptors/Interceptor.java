package com.zqf.customframwork.interceptors;

import com.zqf.customframwork.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Interceptor {

    boolean  doIntercept(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception;
}
