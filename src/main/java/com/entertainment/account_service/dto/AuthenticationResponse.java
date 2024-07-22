package com.entertainment.account_service.dto;

public class AuthenticationResponse {
    private String username;
    private String role;
//    private Integer customerId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public Integer getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(Integer customerId) {
//        this.customerId = customerId;
//    }
}
