package com.shishishi3.filter;

import com.shishishi3.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 授权过滤器 - 整个应用的安全核心
 * 拦截所有请求，并根据预设规则进行认证和授权检查。
 */
@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器初始化，可以在此加载一些配置
        System.out.println("AuthorizationFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // ========== 1. 公共资源放行 ==========
        // 对登录页、登出操作、静态资源（CSS, JS等）不进行任何拦截
        // 请在判断条件中加入 path.equals("/register")
        if (path.equals("/login") || path.equals("/register") || path.equals("/register-verify.jsp") || path.equals("/verify-registration") || path.equals("/logout") || path.startsWith("/css/") || path.startsWith("/js/") || path.equals("/access-denied.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // ========== 2. 认证检查 (是否登录) ==========
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 用户未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ========== 3. 授权检查 (是否有权访问) ==========
        // 如果代码执行到这里，说明用户肯定已经登录了
        User user = (User) session.getAttribute("user");

        // 检查是否是需要管理员权限的路径
        if (path.startsWith("/admin/")) {
            // 我们之前已经有了对 "admin:access" 的整体检查，
            // 为了更精细，我们可以为不同管理页面设置不同权限
            String requiredPermission = "admin:access"; // 默认需要后台访问权限

            if (path.equals("/admin/manage-requests")) {
                requiredPermission = "supply_request:manage"; // 审批申领需要专门的权限
            }

            if (user.hasPermission(requiredPermission)) {
                chain.doFilter(request, response);
            } else {
                handleNoPermission(request, response, requiredPermission);
            }
            return;
        }

        // 在 AuthorizationFilter.java 的 doFilter 方法中
        if (path.equals("/equipment")) {
            String action = request.getParameter("action");
            String requiredPermission = null;

            if (action == null || action.equals("list") || action.equals("view")) {
                requiredPermission = "equipment:view";
            } else if (action.equals("add_form") || action.equals("insert")) {
                requiredPermission = "equipment:create";
            } else if (action.equals("book")) { // 新增对预约操作的权限检查
                requiredPermission = "equipment:book";
            }

            if (requiredPermission != null && !user.hasPermission(requiredPermission)) {
                handleNoPermission(request, response, requiredPermission);
                return;
            }
        }

        // 检查是否是任务模块的路径
        if (path.equals("/tasks")) {
            String action = request.getParameter("action");
            String requiredPermission = null;

            // 根据action参数判断所需权限
            if (action == null || action.equals("list")) {
                requiredPermission = "task:view";
            } else if (action.equals("new") || action.equals("insert")) {
                requiredPermission = "task:create";
            } else if (action.equals("edit") || action.equals("update")) {
                requiredPermission = "task:edit";
            } else if (action.equals("delete")) {
                requiredPermission = "task:delete";
            }

            // 执行权限检查
            if (requiredPermission != null && !user.hasPermission(requiredPermission)) {
                handleNoPermission(request, response, requiredPermission);
                return;
            }
        }

        if (path.equals("/manage-booking")) {
            if (!user.hasPermission("booking:manage")) {
                handleNoPermission(request, response, "booking:manage");
                return;
            }
        }

        if (path.equals("/supplies")) {
            String action = request.getParameter("action");
            String requiredPermission = null;

            if (action == null || action.equals("list")) {
                requiredPermission = "supply:view";
            } else if (action.equals("add_form") || action.equals("insert") || action.equals("update_quantity")) {
                // 将 update_quantity 操作也加入 supply:manage 权限的保护范围
                requiredPermission = "supply:manage";
            }

            if (requiredPermission != null && !user.hasPermission(requiredPermission)) {
                handleNoPermission(request, response, requiredPermission);
                return;
            }
        }

        if (path.equals("/supply-request")) {
            if (!user.hasPermission("supply_request:create")) {
                handleNoPermission(request, response, "supply_request:create");
                return;
            }
        }

        if (path.equals("/venues")) {
            String action = request.getParameter("action");
            String requiredPermission = null;

            if (action == null || action.equals("list") || action.equals("view")) {
                requiredPermission = "venue:view";
            } else if (action.equals("add_form") || action.equals("insert")) {
                requiredPermission = "venue:manage";
            } else if (action.equals("book")) {
                requiredPermission = "venue:book";
            }

            if (requiredPermission != null && !user.hasPermission(requiredPermission)) {
                handleNoPermission(request, response, requiredPermission);
                return;
            }
        }

        // ========== 4. 默认放行 ==========
        // 如果请求的路径不属于任何一个受保护的模块，则默认放行
        chain.doFilter(request, response);
    }

    /**
     * 一个统一处理“无权限”情况的私有方法
     */
    private void handleNoPermission(HttpServletRequest request, HttpServletResponse response, String requiredPermission) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 设置HTTP状态码为 403
        request.setAttribute("message", "抱歉，您没有执行此操作所需的权限 (" + requiredPermission + ")");
        request.getRequestDispatcher("/access-denied.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁
    }
}