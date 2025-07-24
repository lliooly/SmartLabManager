<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${param.pageTitle} - 智能实验室管理平台</title>

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
  <style>
    .card.card-glass {
      background-color: rgba(255, 255, 255, 0.65) !important;
      -webkit-backdrop-filter: blur(10px);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.3);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }
    .table {
      /* 为了让表格也能适应毛玻璃效果，可以给它一个半透明背景 */
      background-color: rgba(255, 255, 255, 0.8);
    }
  </style>
</head>
<body id="page-top">

<jsp:include page="/WEB-INF/layout/navbar.jsp" />

<div class="container mt-3">
</div>