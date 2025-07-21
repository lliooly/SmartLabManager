package com.shishishi3.controller;

import com.shishishi3.dao.VenueDAO;
import com.shishishi3.model.User;
import com.shishishi3.model.Venue;
import com.shishishi3.model.VenueBooking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/venues")
public class VenueServlet extends HttpServlet {
    private VenueDAO venueDAO;

    @Override
    public void init() {
        venueDAO = new VenueDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "list" : action) {
            case "add_form":
                showAddForm(request, response);
                break;
            case "view": // 新增：处理查看详情的请求
                viewVenue(request, response);
                break;
            case "list":
            default:
                listVenues(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertVenue(request, response);
        } else if ("book".equals(action)) { // 新增：处理预约提交的请求
            bookVenue(request, response);
        }
    }

    private void listVenues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ... 此方法保持不变
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ... 此方法保持不变
    }

    private void insertVenue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... 此方法保持不变
    }

    // 新增方法：用于显示场地详情和预约情况
    private void viewVenue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int venueId = Integer.parseInt(request.getParameter("id"));
        Venue venue = venueDAO.getVenueById(venueId);
        List<VenueBooking> bookings = venueDAO.getBookingsForVenue(venueId);

        request.setAttribute("venue", venue);
        request.setAttribute("bookingList", bookings);
        request.getRequestDispatcher("/venue-detail.jsp").forward(request, response);
    }

    // 新增方法：用于处理用户提交的预约
    private void bookVenue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int venueId = Integer.parseInt(request.getParameter("venueId"));
        Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime").replace("T", " ") + ":00");
        Timestamp endTime = Timestamp.valueOf(request.getParameter("endTime").replace("T", " ") + ":00");
        String purpose = request.getParameter("purpose");
        User user = (User) request.getSession().getAttribute("user");

        if (endTime.before(startTime)) {
            response.sendRedirect("venues?action=view&id=" + venueId + "&error=time_error");
            return;
        }

        if (venueDAO.hasBookingConflict(venueId, startTime, endTime)) {
            response.sendRedirect("venues?action=view&id=" + venueId + "&error=conflict");
        } else {
            VenueBooking booking = new VenueBooking();
            booking.setVenueId(venueId);
            booking.setUserId(user.getId());
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setPurpose(purpose);

            venueDAO.createBooking(booking);
            response.sendRedirect("venues?action=view&id=" + venueId + "&success=booked");
        }
    }
}