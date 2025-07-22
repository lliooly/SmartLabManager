<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
    <jsp:param name="pageTitle" value="添加新场地"/>
</jsp:include>

<div class="container main-content">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card card-glass">
                <div class="card-header"><h3><i class="bi bi-plus-circle"></i> 添加新场地</h3></div>
                <div class="card-body">
                    <form action="venues?action=insert" method="post">
                        <div class="form-group"><label for="name">场地名称</label><input type="text" class="form-control" id="name" name="name" required></div>
                        <div class="form-group"><label for="description">描述</label><textarea class="form-control" id="description" name="description" rows="3"></textarea></div>
                        <div class="form-row">
                            <div class="form-group col-md-4"><label for="location">位置</label><input type="text" class="form-control" id="location" name="location"></div>
                            <div class="form-group col-md-4"><label for="capacity">容量 (人)</label><input type="number" class="form-control" id="capacity" name="capacity" value="10" min="0"></div>
                            <div class="form-group col-md-4"><label for="status">初始状态</label><select id="status" name="status" class="form-control"><option selected>可用</option><option>维修中</option><option>已停用</option></select></div>
                        </div>
                        <button type="submit" class="btn btn-primary">保存场地</button>
                        <a href="${pageContext.request.contextPath}/venues" class="btn btn-secondary">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />