package net.javaguides.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.service.RoleService;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository rolerepository;
    
        @Override
        @Transactional
        public Roles createRole(Roles role) {
            return rolerepository.save(role);
        }
    
        @Override
        @Transactional
        public List<Roles> getAllRoles() {
            return rolerepository.findAll();    
        }
}