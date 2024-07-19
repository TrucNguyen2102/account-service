package com.entertainment.account_service.service;

import com.entertainment.account_service.dto.AccountEmployeeDTO;
import com.entertainment.account_service.entity.Account;
import com.entertainment.account_service.entity.AccountStatus;
import com.entertainment.account_service.entity.Role;
import com.entertainment.account_service.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public Account createAccount(String username, String password, Integer roleId) {
//        Account account = new Account();
//        account.setUsername(username);
//        account.setPassword(password);
//        account.setRoleId(roleId);
//        account.setCreatedTime(LocalDateTime.now());
//        account.setUpdatedTime(LocalDateTime.now());
//        return accountRepository.save(account);
//    }

    @Override
    public Account save(Account account) {

        return accountRepository.save(account);
    }

    public Account saveWithRole(Account account, String roleName) {
        Role role = roleService.findByName(roleName);
        if (role != null) {
            account.setRole(role);
        }
        // Mã hóa mật khẩu
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Thiết lập thời gian tạo
        account.setCreatedTime(LocalDateTime.now());
        // Thiết lập thời gian update
        account.setUpdatedTime(LocalDateTime.now());
        account.setStatus(AccountStatus.ACTIVE); // Đặt trạng thái là ACTIVE
        return accountRepository.save(account);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<AccountEmployeeDTO> getAccountsByRoleIds(List<Integer> roleIds) {
        List<Account> accounts = accountRepository.findByRoleIdIn(roleIds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return accounts.stream()
                .map(account -> new AccountEmployeeDTO(
                        account.getUsername(),
                        account.getCreatedTime().format(formatter),
                        account.getUpdatedTime().format(formatter),
                        account.getStatus()))
                .collect(Collectors.toList());

    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public void lockAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            account.setStatus(AccountStatus.valueOf("LOCKED")); // Hoặc trạng thái bạn định nghĩa
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Account not found");
        }
    }

    @Override
    public void unlockAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            account.setStatus(AccountStatus.valueOf("ACTIVE"));
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Tài khoản không tồn tại.");
        }
    }

//    @Override
//    public void lockAccount(String username) {
//        Account account = accountRepository.findByUsername(username);
//        if (account != null) {
//            account.setEnabled(false); // Giả sử bạn dùng cột 'enabled' để biểu thị trạng thái khóa tài khoản
//            accountRepository.save(account);
//        } else {
//            throw new RuntimeException("Tài khoản không tồn tại.");
//        }
//    }

//    @Override
//    public List<AccountEmployeeDTO> getAllAccounts() {
//        List<Account> accounts = accountRepository.findAll();
//        return accounts.stream()
//                .map(account -> new AccountEmployeeDTO(account.getUsername(), account.getCreatedTime(), account.getUpdatedTime()))
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<AccountEmployeeDTO> getAccountsByRoles(List<String> roleNames) {
//        List<Account> accounts = accountRepository.findByRoleNameIn(roleNames);
//        return accounts.stream()
//                .map(account -> new AccountEmployeeDTO(account.getUsername(),
//                        account.getCreatedTime(), account.getUpdatedTime()))
//                .collect(Collectors.toList());
//    }


}
