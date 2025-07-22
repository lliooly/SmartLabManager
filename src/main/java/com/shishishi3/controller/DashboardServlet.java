package com.shishishi3.controller;

import com.shishishi3.dao.*;
import com.shishishi3.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private EquipmentDAO equipmentDAO;
    private VenueDAO venueDAO;

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        equipmentDAO = new EquipmentDAO();
        venueDAO = new VenueDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        int userId = currentUser.getId();

        // 从各个DAO获取当前用户相关的数据
        List<Project> myProjects = projectDAO.getProjectsByCreatorId(userId);
        List<Task> myTasks = taskDAO.getTasksByAssigneeId(userId);
        List<EquipmentBooking> myEquipmentBookings = equipmentDAO.getBookingsForUser(userId);
        List<VenueBooking> myVenueBookings = venueDAO.getBookingsByUserId(userId);

        // 将数据存入request
        request.setAttribute("myProjects", myProjects);
        request.setAttribute("myTasks", myTasks);
        request.setAttribute("myEquipmentBookings", myEquipmentBookings);
        request.setAttribute("myVenueBookings", myVenueBookings);

        // 转发到JSP页面
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}