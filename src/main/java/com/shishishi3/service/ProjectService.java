package com.shishishi3.service;

import com.shishishi3.dao.ProjectDAO;
import com.shishishi3.model.Project;
import com.shishishi3.util.AiService;

public class ProjectService {
    private ProjectDAO projectDAO = new ProjectDAO();

    /**
     * 评估项目风险的核心业务方法
     * @param projectId 项目ID
     * @param additionalNotes 用户补充说明
     * @return AI生成的JSON报告字符串
     */
    public String assessProjectRisk(int projectId, String additionalNotes) {
        // 1. 根据ID获取项目完整信息
        Project project = projectDAO.getProjectById(projectId);

        if (project == null) {
            return "{\"error\":\"找不到ID为 " + projectId + " 的项目\"}";
        }

        // 2. 准备提交给AI服务的内容 (此逻辑从Servlet移至此处)
        String experimentDetails = "项目名称: " + project.getProjectName() + "\n" +
                "项目简述: " + project.getDescription() + "\n" +
                "实验目的: " + project.getPurpose() + "\n" +
                "详细操作步骤: " + project.getProcedureSteps() + "\n" +
                "所用试剂和设备: " + project.getReagentsAndEquipment() + "\n" +
                "额外备注: " + (additionalNotes.isEmpty() ? "无" : additionalNotes);

        // 3. 调用AI服务
        String jsonResponse = AiService.getRiskAssessment(experimentDetails);

        // 4. 将AI返回的报告保存回数据库
        projectDAO.updateRiskAssessmentReport(projectId, jsonResponse);

        // 5. 将报告返回给调用者(Servlet)
        return jsonResponse;
    }
}