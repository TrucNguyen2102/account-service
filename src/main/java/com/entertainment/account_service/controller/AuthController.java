package com.entertainment.account_service.controller;

import com.entertainment.account_service.dto.AuthenticationRequest;
import com.entertainment.account_service.dto.AuthenticationResponse;
import com.entertainment.account_service.entity.Account;
import com.entertainment.account_service.entity.AccountStatus;
import com.entertainment.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountService accountService;



//    public AuthController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest request) {


        try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin tài khoản để trả về
            Account account = accountService.findByUsername(request.getUsername());

            // Kiểm tra trạng thái tài khoản
            if (AccountStatus.LOCKED.equals(account.getStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản của bạn đã bị khóa.");
            }

            String role = account.getRole().getName(); // Lấy vai trò từ tài khoản



            // Tạo phản hồi
            AuthenticationResponse response = new AuthenticationResponse();
            response.setUsername(account.getUsername()); //lấy username từ tài khoản
            response.setRole(role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @GetMapping("/username")
    public ResponseEntity<Account> getAccountByUsername(@RequestParam String username) {
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }




}
