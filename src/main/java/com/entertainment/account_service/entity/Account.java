package com.entertainment.account_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password" , nullable = false, length = 255)
    private String password;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();//thời gian mặc định là hiện tại

    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime = LocalDateTime.now();//thời gian mặc định là hiện tại

//    @Column(name = "status", nullable = false, length = 50)
//    private String status = "ACTIVE";
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private  Role role;

    public Account() {

    }

    public Account(String username, String password, LocalDateTime createdTime, LocalDateTime updatedTime, AccountStatus status, Role role) {
        this.username = username;
        this.password = password;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.status = AccountStatus.ACTIVE;
        this.role = role;
    }

//    public Account(String username, String password, LocalDateTime createdTime, LocalDateTime updatedTime, String status, Role role) {
//        this.username = username;
//        this.password = password;
//        this.createdTime = createdTime;
//        this.updatedTime = updatedTime;
//        this.status = status;
//        this.role = role;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
