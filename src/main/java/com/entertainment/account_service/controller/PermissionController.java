package com.entertainment.account_service.controller;

import com.entertainment.account_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PermissionController {
    @Autowired
    private RoleService roleService;
    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @GetMapping("/check-permission")
    public ResponseEntity<Map<String, Boolean>> checkPermission(Authentication authentication) {
        boolean hasPermission = false;

        // Kiểm tra quyền hạn của người dùng
        if (authentication != null) {
            hasPermission = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("MANAGER"));
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPermission", hasPermission);
        return ResponseEntity.ok(response);
    }
}
