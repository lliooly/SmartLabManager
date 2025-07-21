package com.shishishi3.controller;

import com.shishishi3.dao.EquipmentDAO;
import com.shishishi3.model.EquipmentBooking;
import com.shishishi3.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/manage-booking")
public class BookingManagementServlet extends HttpServlet {
    private EquipmentDAO equipmentDAO;

    @Override
    public void init() {
        equipmentDAO = new EquipmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        User currentUser = (User) request.getSession().getAttribute("user");

        EquipmentBooking booking = equipmentDAO.getBookingById(bookingId);
        if (booking == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "预约记录未找到");
            return;
        }
        int equipmentId = booking.getEquipmentId();

        if ("borrow".equals(action) && currentUser.hasPermission("booking:manage")) {
            // “借出”操作
            equipmentDAO.updateBookingStatus(bookingId, "使用中");
            equipmentDAO.updateEquipmentStatus(equipmentId, "使用中");
        } else if ("return".equals(action) && currentUser.hasPermission("booking:manage")) {
            // “归还”操作
            equipmentDAO.updateBookingStatus(bookingId, "已完成");
            equipmentDAO.updateEquipmentStatus(equipmentId, "可用");
        } else if ("cancel".equals(action)) {
            // “取消”操作 - 增加安全检查
            // 必须是预约者本人，或者是拥有管理权限的用户，才能取消
            if (currentUser.getId() == booking.getUserId() || currentUser.hasPermission("booking:manage")) {
                equipmentDAO.updateBookingStatus(bookingId, "已取消");
            } else {
                // 如果一个非管理员用户试图取消别人的预约，就拒绝他
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有权限取消此预约");
                return;
            }
        }

        // 操作完成后，重定向回设备详情页
        response.sendRedirect("equipment?action=view&id=" + equipmentId);
    }
}