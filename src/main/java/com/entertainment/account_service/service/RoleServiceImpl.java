package com.entertainment.account_service.service;

import com.entertainment.account_service.entity.Role;
import com.entertainment.account_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Integer id, String newName) {
//        Role updateRole = roleRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        Role existingRole = roleRepository.findByName(newName);
//        if (existingRole != null && !existingRole.getId().equals(id)) {
//            throw new RuntimeException("Role name already exists");
//        }
//
//        updateRole.setName(newName);
//
//        return roleRepository.save(updateRole);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(newName);
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Integer id) {
        Optional<Role> role = roleRepository.findById(id); //tìm role theo id
        return role.orElse(null);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }


}
