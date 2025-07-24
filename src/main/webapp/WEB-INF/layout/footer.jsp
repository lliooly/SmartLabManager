<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- 在这里统一引入所有JavaScript文件 --%>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
<script src="https://cdn.jsdelivr.net/npm/echarts@5.5.0/dist/echarts.min.js"></script>

<script>
    // 确保页面加载完成后再执行
    document.addEventListener("DOMContentLoaded", function() {
        // 检查页面上是否存在对应的元素，避免在其他页面报错
        if (document.getElementById('startDate')) {
            flatpickr("#startDate", { altInput: true, altFormat: "Y/m/d", dateFormat: "Y-m-d", locale: "zh" });
        }
        if (document.getElementById('endDate')) {
            flatpickr("#endDate", { altInput: true, altFormat: "Y/m/d", dateFormat: "Y-m-d", locale: "zh" });
        }
    });
</script>

</body>
</html>