package com.shishishi3.controller;

import com.shishishi3.dao.SupplyDAO;
import com.shishishi3.dao.SupplyRequestDAO;
import com.shishishi3.model.Supply;
import com.shishishi3.model.SupplyRequest;
import com.shishishi3.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/supply-request")
public class SupplyRequestServlet extends HttpServlet {
    private SupplyRequestDAO requestDAO;
    private SupplyDAO supplyDAO; // 需要它来获取物资列表

    @Override
    public void init() {
        requestDAO = new SupplyRequestDAO();
        supplyDAO = new SupplyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        action = (action == null) ? "my_list" : action;
        User user = (User) request.getSession().getAttribute("user");

        switch (action) {
            case "show_form":
                // 获取所有物资以供下拉列表选择
                List<Supply> allSupplies = supplyDAO.getAllSupplies();
                request.setAttribute("allSupplies", allSupplies);
                request.getRequestDispatcher("/supply-request-form.jsp").forward(request, response);
                break;
            case "my_list":
            default:
                // 显示我自己的申领列表
                List<SupplyRequest> myRequests = requestDAO.getRequestsByUserId(user.getId());
                request.setAttribute("myRequests", myRequests);
                request.getRequestDispatcher("/my-requests.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");

        int supplyId = Integer.parseInt(request.getParameter("supplyId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String notes = request.getParameter("notes");

        SupplyRequest newRequest = new SupplyRequest();
        newRequest.setSupplyId(supplyId);
        newRequest.setQuantityRequested(quantity);
        newRequest.setNotes(notes);
        newRequest.setRequesterId(user.getId());

        requestDAO.createRequest(newRequest);
        response.sendRedirect("supply-request?action=my_list");
    }
}