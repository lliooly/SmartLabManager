package com.shishishi3.controller;

import com.shishishi3.dao.EquipmentDAO;
import com.shishishi3.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/equipment")
public class EquipmentServlet extends HttpServlet {
    private EquipmentDAO equipmentDAO;

    @Override
    public void init() {
        equipmentDAO = new EquipmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // 根据action参数决定执行哪个操作
        switch (action == null ? "list" : action) {
            case "add_form":
                // 显示添加设备的表单
                showAddForm(request, response);
                break;
            case "view":
                viewEquipment(request, response);
                break;
            case "list":
            default:
                // 默认操作：显示设备列表
                listEquipment(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertEquipment(request, response);
        } else if ("book".equals(action)) {
            bookEquipment(request, response);
        }
    }

    private void listEquipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Equipment> equipmentList = equipmentDAO.getAllEquipment();
        request.setAttribute("equipmentList", equipmentList);
        request.getRequestDispatcher("/equipment-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/equipment-form.jsp").forward(request, response);
    }

    private void insertEquipment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Equipment equipment = new Equipment();
        equipment.setName(request.getParameter("name"));
        equipment.setModel(request.getParameter("model"));
        equipment.setSerialNumber(request.getParameter("serialNumber"));
        equipment.setLocation(request.getParameter("location"));
        equipment.setStatus(request.getParameter("status"));

        // 处理日期，需要判空
        String purchaseDateStr = request.getParameter("purchaseDate");
        if (purchaseDateStr != null && !purchaseDateStr.isEmpty()) {
            equipment.setPurchaseDate(Date.valueOf(purchaseDateStr));
        }
        String maintDateStr = request.getParameter("lastMaintenanceDate");
        if (maintDateStr != null && !maintDateStr.isEmpty()) {
            equipment.setLastMaintenanceDate(Date.valueOf(maintDateStr));
        }

        equipmentDAO.addEquipment(equipment);

        // 操作完成后，重定向回设备列表页面
        response.sendRedirect(request.getContextPath() + "/equipment");
    }

    private void bookEquipment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int equipmentId = Integer.parseInt(request.getParameter("equipmentId"));
        Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime").replace("T", " ") + ":00");
        Timestamp endTime = Timestamp.valueOf(request.getParameter("endTime").replace("T", " ") + ":00");
        String purpose = request.getParameter("purpose");
        User user = (User) request.getSession().getAttribute("user");

        // 核心逻辑：先检查时间是否冲突
        if (equipmentDAO.hasBookingConflict(equipmentId, startTime, endTime)) {
            // 存在冲突，重定向回详情页并附带错误信息
            response.sendRedirect("equipment?action=view&id=" + equipmentId + "&error=conflict");
        } else {
            // 没有冲突，创建预约
            EquipmentBooking booking = new EquipmentBooking();
            booking.setEquipmentId(equipmentId);
            booking.setUserId(user.getId());
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setPurpose(purpose);

            equipmentDAO.createBooking(booking);

            // 预约成功，重定向回详情页并附带成功信息
            response.sendRedirect("equipment?action=view&id=" + equipmentId + "&success=booked");
        }
    }

    private void viewEquipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String returnUrl = request.getParameter("returnUrl"); // 新增：获取returnUrl参数

        Equipment equipment = equipmentDAO.getEquipmentById(id);
        List<EquipmentBooking> bookings = equipmentDAO.getBookingsForEquipment(id);

        request.setAttribute("equipment", equipment);
        request.setAttribute("bookingList", bookings);
        request.setAttribute("returnUrl", returnUrl); // 新增：将returnUrl传递给JSP页面

        request.getRequestDispatcher("/equipment-detail.jsp").forward(request, response);
    }
}