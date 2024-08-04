package com.entertainment.account_service.repository;

import com.entertainment.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
//    Optional<Account> findByUsername(String username);

//    List<Account> findByRoleNameIn(List<String> roleNames);
    List<Account> findByRoleIdIn(List<Integer> roleIds);

    boolean existsByUsername(String username);
}
