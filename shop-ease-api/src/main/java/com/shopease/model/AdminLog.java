package com.shopease.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdminLog implements Serializable {
    private int logId;
    private int adminId;
    private String action;
    private String details;
    private Timestamp createdAt;

    public AdminLog() {}

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
