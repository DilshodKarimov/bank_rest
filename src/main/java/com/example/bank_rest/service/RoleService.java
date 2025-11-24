package com.example.bank_rest.service;


import com.example.bank_rest.entity.Role;
import com.example.bank_rest.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private void createRoleIfNotExists(String roleName){
        if(!roleRepository.findByName(roleName).isPresent()){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER").get();
    }
    public Role getAdminRole(){
        return roleRepository.findByName("ROLE_ADMIN").get();
    }


}
