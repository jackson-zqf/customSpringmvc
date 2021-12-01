package com.zqf.demo.controller;

import com.zqf.customframwork.annotations.Autowired;
import com.zqf.customframwork.annotations.Controller;
import com.zqf.customframwork.annotations.RequestMapping;
import com.zqf.customframwork.annotations.Security;
import com.zqf.demo.service.CustomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/demo")
@Security({"root01","root02"})
public class CustomController {

    @Autowired
    private CustomService customServiceImpl;


    @RequestMapping("/query01")
    @Security({"root01","root02","root03"})
    public String query01(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        response.getWriter().write(username+"用户正在访问。。。。。。");
        return customServiceImpl.getName(username);
    }

    @RequestMapping("/query02")
    @Security("root02")
    public String query02(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        response.getWriter().write(username+"用户正在访问。。。。。。");
        return customServiceImpl.getName(username);
    }

    @RequestMapping("/query03")
    @Security("root03")
    public String query03(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        response.getWriter().write(username+"用户正在访问。。。。。。");
        return customServiceImpl.getName(username);
    }
}
