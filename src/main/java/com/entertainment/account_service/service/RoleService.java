package com.entertainment.account_service.service;

import com.entertainment.account_service.entity.Role;

import java.util.List;

public interface RoleService {
    Role save(Role role); //tạo role
    Role updateRole(Integer id, String newName);

    List<Role> findAll(); //lấy tất cả role
    Role findById(Integer id);
    Role findByName(String name);
}
