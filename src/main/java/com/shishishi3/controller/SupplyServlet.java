package com.shishishi3.controller;

import com.shishishi3.dao.SupplyDAO;
import com.shishishi3.model.Supply;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/supplies")
public class SupplyServlet extends HttpServlet {
    private SupplyDAO supplyDAO;

    @Override
    public void init() {
        supplyDAO = new SupplyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "list" : action) {
            case "add_form":
                showAddForm(request, response);
                break;
            case "list":
            default:
                listSupplies(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertSupply(request, response);
        } else if ("update_quantity".equals(action)) { // 新增：处理更新库存的逻辑
            updateSupplyQuantity(request, response);
        }
    }

    private void listSupplies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Supply> supplyList = supplyDAO.getAllSupplies();
        request.setAttribute("supplyList", supplyList);
        request.getRequestDispatcher("/supply-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/supply-form.jsp").forward(request, response);
    }

    private void insertSupply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (此方法保持不变)
    }

    // 新增方法：用于更新库存数量
    private void updateSupplyQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int supplyId = Integer.parseInt(request.getParameter("supplyId"));
        int newQuantity = Integer.parseInt(request.getParameter("newQuantity"));

        supplyDAO.updateSupplyQuantity(supplyId, newQuantity);

        response.sendRedirect(request.getContextPath() + "/supplies");
    }
}