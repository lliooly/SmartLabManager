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
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add_form":
                showAddForm(request, response);
                break;
            case "view":
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
        } else if ("book".equals(action)) {
            bookVenue(request, response);
        }
    }

    private void listVenues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Venue> venueList = venueDAO.getAllVenues();
        request.setAttribute("venueList", venueList);
        request.getRequestDispatcher("/venue-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/venue-form.jsp").forward(request, response);
    }

    private void viewVenue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int venueId = Integer.parseInt(request.getParameter("id"));
        String returnUrl = request.getParameter("returnUrl");

        Venue venue = venueDAO.getVenueById(venueId);
        List<VenueBooking> bookings = venueDAO.getBookingsForVenue(venueId);

        request.setAttribute("venue", venue);
        request.setAttribute("bookingList", bookings);
        request.setAttribute("returnUrl", returnUrl);

        request.getRequestDispatcher("/venue-detail.jsp").forward(request, response);
    }

    private void insertVenue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Venue venue = new Venue();
        venue.setName(request.getParameter("name"));
        venue.setDescription(request.getParameter("description"));
        venue.setLocation(request.getParameter("location"));
        venue.setCapacity(Integer.parseInt(request.getParameter("capacity")));
        venue.setStatus(request.getParameter("status"));

        venueDAO.addVenue(venue);

        response.sendRedirect(request.getContextPath() + "/venues");
    }

    private void bookVenue(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int venueId = Integer.parseInt(request.getParameter("venueId"));
        Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime").replace("T", " ") + ":00");
        Timestamp endTime = Timestamp.valueOf(request.getParameter("endTime").replace("T", " ") + ":00");
        String purpose = request.getParameter("purpose");
        User user = (User) request.getSession().getAttribute("user");

        if (endTime.before(startTime)) {
            request.getSession().setAttribute("errorMessage", "预约失败：结束时间不能早于开始时间。");
            response.sendRedirect("venues?action=view&id=" + venueId);
            return;
        }

        if (venueDAO.hasBookingConflict(venueId, startTime, endTime)) {
            request.getSession().setAttribute("errorMessage", "预约失败：您选择的时间段与现有预约冲突。");
            response.sendRedirect("venues?action=view&id=" + venueId);
        } else {
            VenueBooking booking = new VenueBooking();
            booking.setVenueId(venueId);
            booking.setUserId(user.getId());
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setPurpose(purpose);

            venueDAO.createBooking(booking);
            request.getSession().setAttribute("successMessage", "场地预约成功！");
            response.sendRedirect("venues?action=view&id=" + venueId);
        }
    }
}