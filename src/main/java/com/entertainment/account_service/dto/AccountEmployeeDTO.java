package com.entertainment.account_service.dto;

import com.entertainment.account_service.entity.AccountStatus;

import java.time.LocalDateTime;

public class AccountEmployeeDTO {

    private String username;
    private String createdTime;
    private String updatedTime;
    private AccountStatus status;

    public AccountEmployeeDTO(String username, String createdTime, String updatedTime, AccountStatus status) {
        this.username = username;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
