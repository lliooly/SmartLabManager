package com.shishishi3.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Project {

    private int id;
    private String projectName;
    private String description;
    private String status; // '申请中', '进行中', '已完成', '已归档'
    private int creatorId;
    private Date startDate;
    private Date endDate;
    private Timestamp createdAt;
    private String purpose;
    private String procedureSteps;
    private String reagentsAndEquipment;
    private String riskAssessmentReport;

    // 非数据库字段，用于方便在JSP中显示创建者姓名
    private String creatorName;

    public Project() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCreatorId() { return creatorId; }
    public void setCreatorId(int creatorId) { this.creatorId = creatorId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getProcedureSteps() {
        return procedureSteps;
    }
    public void setProcedureSteps(String procedureSteps) {
        this.procedureSteps = procedureSteps;
    }

    public String getReagentsAndEquipment() {
        return reagentsAndEquipment;
    }
    public void setReagentsAndEquipment(String reagentsAndEquipment) {
        this.reagentsAndEquipment = reagentsAndEquipment;
    }

    public String getRiskAssessmentReport() {
        return riskAssessmentReport;
    }
    public void setRiskAssessmentReport(String riskAssessmentReport) {
        this.riskAssessmentReport = riskAssessmentReport;
    }
}