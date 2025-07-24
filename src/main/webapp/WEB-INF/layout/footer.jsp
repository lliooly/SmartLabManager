<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- 引入所有第三方JavaScript文件 --%>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
<script src="https://cdn.jsdelivr.net/npm/echarts@5.5.0/dist/echarts.min.js"></script>

<%-- CSS动画定义 --%>
<style>
    @keyframes gradientAnimation {
        0% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
        100% { background-position: 0% 50%; }
    }
</style>

<%-- JavaScript应用脚本 --%>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        // 选中body元素
        const body = document.body;

        // 直接通过style属性设置样式，这是最高优先级
        // 您可以在这里修改为您喜欢的配色方案，但请务必保证语法正确
        body.style.background = 'linear-gradient(-45deg, #f7cac9, #f6f7c9, #c9f6f7, #cac9f7)';

        body.style.backgroundSize = '250% 250%';
        body.style.animation = 'gradientAnimation 45s ease-in-out infinite';
        body.style.backgroundAttachment = 'fixed';

        // flatpickr的初始化代码
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