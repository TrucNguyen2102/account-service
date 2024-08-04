package com.entertainment.account_service.controller;

import com.entertainment.account_service.dto.*;
import com.entertainment.account_service.entity.Account;
import com.entertainment.account_service.entity.AccountStatus;
import com.entertainment.account_service.repository.AccountRepository;
import com.entertainment.account_service.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    //đăng nhập
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
            response.setRole(role); //lấy role từ tài khoản
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    //lấy tài khoản bằng username
    @GetMapping("/username")
    public ResponseEntity<Account> getAccountByUsername(@RequestParam String username) {
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

//    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @PostMapping("/signup/customer")
    public ResponseEntity<Account> signupCustomerAccount(@RequestBody SignUpDTO signUpDTO) {

        Account account = new Account();
        account.setUsername(signUpDTO.getUsername());
        account.setPassword(signUpDTO.getPassword());


        Account savedAccount = accountService.saveWithRole(account, "CUSTOMER");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    //đăng ký tài khoản nhân viên
    @PostMapping("/signup/employee")
    public ResponseEntity<?> createEmployeeAccount(@RequestBody SignUpEmployeeDTO employeeDTO) {
        try {
            if (accountService.existsByUsername(employeeDTO.getUsername())) {
                return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
            }
            Account account = new Account();
            account.setUsername(employeeDTO.getUsername());
            account.setPassword(employeeDTO.getPassword());


            Account savedAccount = accountService.saveWithRole(account, "EMPLOYEE");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tạo tài khoản thất bại");
        }

    }


    @PostMapping("/signup/manager")
    public ResponseEntity<Account> createaManagerAccount(@RequestBody SignUpDTO signUpDTO) {
        Account account = new Account();
        account.setUsername(signUpDTO.getUsername());
        account.setPassword(signUpDTO.getPassword());

        Account savedAccount = accountService.saveWithRole(account, "MANAGER");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    //hiển thị danh sách nhân viên có role là 2(employee)
    @GetMapping("/all/employees")
    public ResponseEntity<List<AccountEmployeeDTO>> getAccountsByRoles() {
        List<AccountEmployeeDTO> accounts = accountService.getAccountsByRoleIds(Arrays.asList(2));
        return ResponseEntity.ok(accounts);
    }

    //hiển thị danh sách khách hàng có role là 3
    @GetMapping("/all/customers")
    public ResponseEntity<List<AccountEmployeeDTO>> getAccountsByCustomerRole() {
        List<AccountEmployeeDTO> accounts = accountService.getAccountsByRoleIds(Arrays.asList(3));
        return ResponseEntity.ok(accounts);
    }

    //cập nhật thời gian cập nhật khi có sự thay đổi thông tin cá nhân
    @PutMapping("/updateTime/{username}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<String> updateAccountTime(@PathVariable String username) {
        try {
            accountService.updateAccountTime(username);
            return ResponseEntity.ok("Time updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    //khóa tài khoản nhân viên
    @PutMapping("/lock/{username}")
    public ResponseEntity<?> lockAccount(@PathVariable String username) {
        try {
            accountService.lockAccount(username); // Implement this in your service layer
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to lock account.");
        }
    }

    //mở lại
    @PutMapping("/unlock/{username}")
    public ResponseEntity<?> unlockAccount(@PathVariable String username) {
        try {
            accountService.unlockAccount(username); // Implement this in your service layer
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unlock account.");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        try {
            accountService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok().body("Đổi mật khẩu thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
