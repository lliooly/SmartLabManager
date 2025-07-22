package com.shishishi3.controller;

import com.google.gson.Gson;
import com.shishishi3.dao.EquipmentDAO;
import com.shishishi3.dao.ProjectDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/dashboard-data")
public class DashboardDataServlet extends HttpServlet {
    private ProjectDAO projectDAO;
    private EquipmentDAO equipmentDAO;
    // private SupplyDAO supplyDAO; // 为未来扩展预留

    private final Gson gson = new Gson();

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
        equipmentDAO = new EquipmentDAO();
        // supplyDAO = new SupplyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 创建一个Map来存放所有图表的数据
        Map<String, Object> dashboardData = new HashMap<>();

        // 2. 从各个DAO获取统计数据
        Map<String, Integer> projectStatusCounts = projectDAO.getProjectStatusCounts();
        Map<String, Integer> monthlyBookingCounts = equipmentDAO.getMonthlyBookingCounts();
        // Map<String, Integer> topSupplies = supplyDAO.getTopRequestedSupplies();

        // 3. 将获取到的数据放入主Map中
        dashboardData.put("projectStatus", projectStatusCounts);
        dashboardData.put("monthlyBookings", monthlyBookingCounts);
        // dashboardData.put("topSupplies", topSupplies);

        // 4. 使用Gson将整个Map转换成JSON字符串
        String jsonData = gson.toJson(dashboardData);

        // 5. 设置响应类型为JSON，并写回给前端
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
    }
}