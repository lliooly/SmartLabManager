package com.shishishi3.controller;

import com.shishishi3.dao.EquipmentDAO;
import com.shishishi3.model.Equipment;
import com.shishishi3.model.EquipmentBooking;
import com.shishishi3.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/equipment")
public class EquipmentServlet extends HttpServlet {
    private EquipmentDAO equipmentDAO;
    private static final Logger logger = LoggerFactory.getLogger(EquipmentServlet.class);

    @Override
    public void init() {

        equipmentDAO = new EquipmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add_form":
                    showAddForm(request, response);
                    break;
                case "view":
                    viewEquipment(request, response);
                    break;
                case "list":
                default:
                    listEquipment(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "加载设备页面时发生未知错误，请稍后重试。");
            response.sendRedirect("dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String redirectUrl = "equipment";

        try {
            if ("insert".equals(action)) {
                insertEquipment(request, response);
                return;
            } else if ("book".equals(action)) {
                bookEquipment(request, response);
                return;
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred in doPost of EquipmentServlet", e);
            request.getSession().setAttribute("errorMessage", "执行操作时发生未知错误，请查看服务器日志。");
            String equipmentId = request.getParameter("equipmentId");
            if (equipmentId != null && !equipmentId.isEmpty()) {
                redirectUrl = "equipment?action=view&id=" + equipmentId;
            }
            response.sendRedirect(redirectUrl);
        }
    }

    // --- 私有辅助方法 ---

    private void listEquipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Equipment> equipmentList = equipmentDAO.getAllEquipment();
        request.setAttribute("equipmentList", equipmentList);
        request.getRequestDispatcher("/equipment-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/equipment-form.jsp").forward(request, response);
    }

    private void viewEquipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String returnUrl = request.getParameter("returnUrl");
        Equipment equipment = equipmentDAO.getEquipmentById(id);
        List<EquipmentBooking> bookings = equipmentDAO.getBookingsForEquipment(id);
        request.setAttribute("equipment", equipment);
        request.setAttribute("bookingList", bookings);
        request.setAttribute("returnUrl", returnUrl);
        request.getRequestDispatcher("/equipment-detail.jsp").forward(request, response);
    }

    private void insertEquipment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Equipment equipment = new Equipment();
        equipment.setName(request.getParameter("name"));
        equipment.setModel(request.getParameter("model"));
        equipment.setSerialNumber(request.getParameter("serialNumber"));
        equipment.setLocation(request.getParameter("location"));
        equipment.setStatus(request.getParameter("status"));
        String purchaseDateStr = request.getParameter("purchaseDate");
        if (purchaseDateStr != null && !purchaseDateStr.isEmpty()) {
            equipment.setPurchaseDate(Date.valueOf(purchaseDateStr));
        }
        String maintDateStr = request.getParameter("lastMaintenanceDate");
        if (maintDateStr != null && !maintDateStr.isEmpty()) {
            equipment.setLastMaintenanceDate(Date.valueOf(maintDateStr));
        }
        equipmentDAO.addEquipment(equipment);
        request.getSession().setAttribute("successMessage", "新设备添加成功！");
        response.sendRedirect(request.getContextPath() + "/equipment");
    }

    // --- 修正后的 bookEquipment 方法 ---
    private void bookEquipment(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        logger.info("--- bookEquipment method started ---");

        String equipmentIdParam = request.getParameter("equipmentId");
        String startTimeParam = request.getParameter("startTime");
        String endTimeParam = request.getParameter("endTime");

        logger.debug("Received parameters -> equipmentId: [{}], startTime: [{}], endTime: [{}]", equipmentIdParam, startTimeParam, endTimeParam);

        int equipmentId = 0;
        Timestamp startTime = null;
        Timestamp endTime = null;
        try {
            equipmentId = Integer.parseInt(equipmentIdParam);
            startTime = Timestamp.valueOf(startTimeParam.replace("T", " ") + ":00");
            endTime = Timestamp.valueOf(endTimeParam.replace("T", " ") + ":00");
            String purpose = request.getParameter("purpose");
            User user = (User) request.getSession().getAttribute("user");

            if (endTime.before(startTime)) {
                logger.warn("Validation FAILED for booking: End time is before start time. User: {}", user.getUsername());
                request.getSession().setAttribute("errorMessage", "预约失败：结束时间不能早于开始时间。");
                response.sendRedirect("equipment?action=view&id=" + equipmentId);
                return;
            }

            logger.debug("Checking for booking conflicts for equipmentId: {}", equipmentId);
            if (equipmentDAO.hasBookingConflict(equipmentId, startTime, endTime)) {
                logger.warn("Validation FAILED for booking: Time conflict found for equipmentId: {}. User: {}", equipmentId, user.getUsername());
                request.getSession().setAttribute("errorMessage", "预约失败：您选择的时间段与现有预约冲突。");
                response.sendRedirect("equipment?action=view&id=" + equipmentId);
            } else {
                logger.info("No conflicts found. Proceeding to create booking for equipmentId: {}", equipmentId);
                EquipmentBooking booking = new EquipmentBooking();
                booking.setEquipmentId(equipmentId);
                booking.setUserId(user.getId());
                booking.setStartTime(startTime);
                booking.setEndTime(endTime);
                booking.setPurpose(purpose);

                equipmentDAO.createBooking(booking);

                request.getSession().setAttribute("successMessage", "设备预约成功！");
                response.sendRedirect("equipment?action=view&id=" + equipmentId);
            }
        } catch (NumberFormatException e) {
            logger.error("CRITICAL ERROR: Failed to parse equipmentId parameter. Value was: '{}'", equipmentIdParam, e);
            request.getSession().setAttribute("errorMessage", "操作失败：提交的设备ID无效。");
            response.sendRedirect("equipment");
        } catch (IllegalArgumentException e) {
            logger.error("CRITICAL ERROR: Failed to parse date/time string. StartTime='{}', EndTime='{}'", startTimeParam, endTimeParam, e);
            request.getSession().setAttribute("errorMessage", "操作失败：提交的日期或时间格式不正确。");
            String redirectUrl = (equipmentId > 0) ? "equipment?action=view&id=" + equipmentId : "equipment";
            response.sendRedirect(redirectUrl);
        }
    }
}