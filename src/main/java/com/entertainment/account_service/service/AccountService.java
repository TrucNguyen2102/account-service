package com.entertainment.account_service.service;

import com.entertainment.account_service.dto.AccountEmployeeDTO;
import com.entertainment.account_service.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface AccountService {
//    Account createAccount(String username, String password, Integer roleId);
//    Account findById(Integer id);
//    Account findByUsername(String username);
    Account save(Account account);

    Account saveWithRole(Account account, String customer);


    Account findByUsername(String username);


//    List<Account> findAllByRoles(String roleName);
    List<AccountEmployeeDTO> getAccountsByRoleIds(List<Integer> roleIds);

    boolean existsByUsername(String username);

    void lockAccount(String username);
    void unlockAccount(String username);
//    List<AccountEmployeeDTO> getAllAccounts();

    void changePassword(String username, String oldPassword, String newPassword) throws Exception;
    void updateAccountTime(String username);
}
