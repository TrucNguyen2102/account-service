package com.entertainment.account_service.init;

import com.entertainment.account_service.entity.Account;
import com.entertainment.account_service.entity.Role;
import com.entertainment.account_service.repository.AccountRepository;
import com.entertainment.account_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminAccount implements CommandLineRunner {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        //kiểm tra xem tài khoản quản lý đã tồn tại chưa
//        Account adminAccount = accountRepository.findByUsername("admin");
//        if (adminAccount == null) {
//            //tạo tài khoản quản lý
//            adminAccount = new Account();
//            adminAccount.setUsername("admin");
//            adminAccount.setPassword(passwordEncoder.encode("123")); // Mã hóa mật khẩu
//
//            // Lấy vai trò từ DB
//            Role managerRole = roleRepository.findByName("MANAGER");
//            if (managerRole != null) {
//                adminAccount.setRole(managerRole); // Gán vai trò
//            } else {
//                System.out.println("Vai trò 'MANAGER' không tồn tại trong cơ sở dữ liệu.");
//                return;
//            }
//
//            accountRepository.save(adminAccount);
//            System.out.println("Tài khoản quản lý mặc định đã được tạo.");
//        }
        // Kiểm tra xem tài khoản đã tồn tại chưa
        Account adminAccount = accountRepository.findByUsername("manager");
        if (adminAccount == null) {
            // Tạo tài khoản quản lý
            Role managerRole = roleRepository.findByName("MANAGER");
            if (managerRole != null) {
                adminAccount = new Account();
                adminAccount.setUsername("manager");
                // Mã hóa mật khẩu
                adminAccount.setPassword(passwordEncoder.encode("123")); // Đảm bảo mã hóa mật khẩu
                adminAccount.setRole(managerRole); // Gán vai trò là MANAGER


                accountRepository.save(adminAccount);
                System.out.println("Tài khoản quản lý đã được tạo.");
            }
        } else {
            System.out.println("Tài khoản quản lý đã tồn tại, không tạo mới.");
        }
    }
}
