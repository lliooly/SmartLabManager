package com.shishishi3.util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 全站字符编码过滤器
 * 使用 @WebFilter("/*") 注解，表示它将拦截所有的请求。
 */
@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器初始化
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. 强制设置所有请求的编码为 UTF-8
        request.setCharacterEncoding("UTF-8");

        // 2. 强制设置所有响应的编码为 UTF-8
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 3. 将请求传递给下一个过滤器或目标 Servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁
    }
}
