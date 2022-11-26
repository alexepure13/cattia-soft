package com.cattia.service;

import com.cattia.model.UserRole;
import com.cattia.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<UserRole> listAllRoles() {
        return roleRepository.findAll();
    }

    public void saveRole(UserRole operator) {
        roleRepository.save(operator);
    }


}
