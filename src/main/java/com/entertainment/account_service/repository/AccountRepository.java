package com.entertainment.account_service.repository;

import com.entertainment.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

//    List<Account> findByRoleNameIn(List<String> roleNames);
    List<Account> findByRoleIdIn(List<Integer> roleIds);

    boolean existsByUsername(String username);
}
