package com.shishishi3.model;

import java.sql.Date;

/**
 * 任务模型 (JavaBean)
 * 用于封装从 lab_tasks 表中获取的数据。
 * 类的属性与数据表字段一一对应。
 */
public class Task {

    private int taskId;
    private int projectId;
    private int userId;
    private String taskName;
    private String taskDescription;
    private Date dueDate;
    private String priority;
    private String status;

    // 为了方便使用，通常会提供一个无参构造函数
    public Task() {
    }

    // 以及一个包含所有参数的构造函数
    public Task(int taskId, int projectId, int userId, String taskName, String taskDescription, Date dueDate, String priority, String status) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.userId = userId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    // --- Getter 和 Setter 方法 ---
    // 这些方法用于获取和设置对象的属性值

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
