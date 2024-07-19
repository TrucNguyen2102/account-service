package com.entertainment.account_service.controller;

import com.entertainment.account_service.dto.AccountEmployeeDTO;
import com.entertainment.account_service.dto.SignUpDTO;
import com.entertainment.account_service.dto.SignUpEmployeeDTO;
import com.entertainment.account_service.entity.Account;
import com.entertainment.account_service.repository.AccountRepository;
import com.entertainment.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @PostMapping("/signup/customer")
    public ResponseEntity<Account> signupCustomerAccount(@RequestBody SignUpDTO signUpDTO) {

        Account account = new Account();
        account.setUsername(signUpDTO.getUsername());
        account.setPassword(signUpDTO.getPassword());


        Account savedAccount = accountService.saveWithRole(account, "CUSTOMER");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @PostMapping("/signup/employee")
    public ResponseEntity<?> createEmployeeAccount(@RequestBody SignUpEmployeeDTO employeeDTO) {
        if (accountService.existsByUsername(employeeDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        Account account = new Account();
        account.setUsername(employeeDTO.getUsername());
        account.setPassword(employeeDTO.getPassword());


        Account savedAccount = accountService.saveWithRole(account, "EMPLOYEE");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @PostMapping("/signup/manager")
    public ResponseEntity<Account> createaManagerAccount(@RequestBody SignUpDTO signUpDTO) {
        Account account = new Account();
        account.setUsername(signUpDTO.getUsername());
        account.setPassword(signUpDTO.getPassword());

        Account savedAccount = accountService.saveWithRole(account, "MANAGER");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    //hiển thị danh sách nhân viên có role là 2(employee)
    @CrossOrigin("http://localhost:3000") //gọi đến front-end
    @GetMapping("/all/employees")
    public ResponseEntity<List<AccountEmployeeDTO>> getAccountsByRoles() {
        List<AccountEmployeeDTO> accounts = accountService.getAccountsByRoleIds(Arrays.asList(2));
        return ResponseEntity.ok(accounts);
    }

    //hiển thị danh sách khách hàng có role là 3
    @CrossOrigin("http://localhost:3000") // Gọi đến front-end
    @GetMapping("/all/customers")
    public ResponseEntity<List<AccountEmployeeDTO>> getAccountsByCustomerRole() {
        List<AccountEmployeeDTO> accounts = accountService.getAccountsByRoleIds(Arrays.asList(3));
        return ResponseEntity.ok(accounts);
    }

    //cập nhật thời gian cập nhật khi có sự thay đổi thông tin cá nhân
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateTime/{username}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Account> updateAccountTime(@PathVariable String username) {
        Account account = accountService.findByUsername(username);
        if (account != null) {
            account.setUpdatedTime(LocalDateTime.now()); // Cập nhật thời gian
            accountService.save(account);
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    //khóa tài khoản nhân viên
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/lock/{username}")
//    public ResponseEntity<String> lockAccount(@PathVariable String username) {
//        try {
//            accountService.lockAccount(username);
//            return ResponseEntity.ok("Tài khoản đã bị khóa.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi khóa tài khoản.");
//        }
//    }
    public ResponseEntity<?> lockAccount(@PathVariable String username) {
        try {
            accountService.lockAccount(username); // Implement this in your service layer
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to lock account.");
        }
    }

    //mở lại
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/unlock/{username}")
//    public ResponseEntity<String> unlockAccount(@PathVariable String username) {
//        try {
//            accountService.unlockAccount(username);
//            return ResponseEntity.ok("Tài khoản đã được mở khóa thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi mở khóa tài khoản.");
//        }
//    }
    public ResponseEntity<?> unlockAccount(@PathVariable String username) {
        try {
            accountService.unlockAccount(username); // Implement this in your service layer
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unlock account.");
        }
    }


}
