package com.shishishi3.controller.admin;

import com.shishishi3.dao.EquipmentDAO;
import com.shishishi3.model.Equipment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/maintenance")
public class MaintenanceServlet extends HttpServlet {
    private EquipmentDAO equipmentDAO;

    @Override
    public void init() {
        equipmentDAO = new EquipmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求用于显示所有设备的列表
        List<Equipment> equipmentList = equipmentDAO.getAllEquipment();
        request.setAttribute("equipmentList", equipmentList);
        request.getRequestDispatcher("/admin/maintenance-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // POST请求用于更新单个设备的维保信息
        int equipmentId = Integer.parseInt(request.getParameter("equipmentId"));
        Date maintDate = Date.valueOf(request.getParameter("lastMaintenanceDate"));
        String status = request.getParameter("status");

        equipmentDAO.updateMaintenanceInfo(equipmentId, maintDate, status);

        // 操作完成后，重定向回维保列表页面
        response.sendRedirect(request.getContextPath() + "/admin/maintenance");
    }
}