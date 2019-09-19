package com.ljj.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getServletPath();
        //System.out.println(path);
        if (path.equals("/login.jsp") || path.equals("/login")|| path.equals("/testVue.jsp")|| path.indexOf("js")>0 || request.getSession().getAttribute("logined") != null)
            filterChain.doFilter(servletRequest, servletResponse);
        else { //重定向到login
            ((HttpServletResponse) servletResponse).sendRedirect("/bbs_web/login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
