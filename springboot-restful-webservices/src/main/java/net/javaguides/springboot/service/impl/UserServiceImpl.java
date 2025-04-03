package net.javaguides.springboot.service.impl;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.entity.UserCsvRepresentation;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Override
    public ResponseEntity<User> loginUser(String username, String password) {
        User user = userrepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword()) || password=="") {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
           return ResponseEntity.ok(user);
        }
    }

    private UserRepository userrepository;
    private RoleRepository rolerepository;
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userrepository.save(user);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userrepository.findAll();
    }

    @Transactional
    public List<Roles> getUserRoles(Long id)
    {
        User user=userrepository.findById(id).orElseThrow(()->new RuntimeException("USER OT FOUND"));        
        return user.getRoles();
    }

    public String modifyUserRole(Long userId, String roleName, String action) {
        User user = userrepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
        if("ROLE_SUPER_ADMIN".equals(roleName))
        {
            return "Cannot modify super admin role";
        }
        Roles role = rolerepository.findByRoleName(roleName)
        .orElseThrow(() -> new RuntimeException("Role not found"));
        if ("assign".equalsIgnoreCase(action)) {
        if (!user.getRoles().contains(role)) {
        user.getRoles().add(role);
        userrepository.save(user);
        return "Role assigned successfully!";
        }
        return "User already has this role";
        }
         
        if ("remove".equalsIgnoreCase(action)) {
        if (user.getRoles().contains(role)) {
        user.getRoles().remove(role);
        userrepository.save(user);
        return "Role removed successfully!";
        }
        return "User does not have this role";
        }
         
        return "Invalid action. Use 'assign' or 'remove'";
        }

    @Override
    public Optional<User> getUser(Long id) {
        Optional<User> user=userrepository.findById(id);
        return user;
    }

    @Override
    public ResponseEntity<String> changePassword(Long id, String oldPassword, String newPassword) 
    {
        @SuppressWarnings("unused")
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 
        User user = userrepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(newPassword!="")
        {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }
        else if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("New Password cannot be similar to the Old password");
        }
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("New password cannot be empty");
        }
 
        String encryptedPassword = passwordEncoder.encode(newPassword);
        
        user.setPassword(encryptedPassword);
        userrepository.save(user);
 
        return ResponseEntity.ok("Password updated successfully!");
    }


    @Override
    public int updateUserStatus(Long id, String status) {
        Optional<User> optuser=userrepository.findById(id);
        if(id==1)
        {
            return 1;
        }
        else {
        if(optuser.isPresent()) {
            User user = optuser.get();
            user.setStatus(status);            
            userrepository.save(user);
        }
    }
        return 0;
    }

    @Transactional
    public void processUserRecords(List<UserCsvRepresentation> records) {
        for (UserCsvRepresentation record : records) {
            // Check if user exists
            Optional<User> existingUser = userrepository.findUserByUsername(record.getUsername());
            
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
                user.setFirstname(record.getFirstname());
                user.setLastname(record.getLastname());
                user.setEmail(record.getEmail());
                user.setStatus(record.getStatus());
            } else {
                user = new User();
                user.setUsername(record.getUsername());
                user.setFirstname(record.getFirstname());
                user.setLastname(record.getLastname());
                user.setEmail(record.getEmail());
                user.setStatus(record.getStatus());
                
                // Set a default password
                String defaultPassword = "welcome@123";
                user.setPassword(passwordEncoder.encode(defaultPassword));
            }
            
            // Process roles
            List<Roles> roles = new ArrayList<>();
            String[] roleNames = record.getRoles().split("\\|");
            
            for (String roleName : roleNames) {
                roleName = roleName.trim();
                Optional<Roles> existingRole = rolerepository.findByRoleName(roleName);
            if (!existingRole.isPresent()) {
                // Log that the role doesn't exist and is being skipped
                Roles role=new Roles();
                role.setRoleName(roleName);
                String rn=role.getRoleName();
                role.setDescription("Role Added form CSV");
                role.setDisplayname(rn.substring(5));
                rolerepository.save(role);
                roles.add(role);
            }
              else{      
            roles.add(existingRole.get());
            }
        }
            if (!roles.isEmpty()) {
                user.setRoles(roles);
                userrepository.save(user);
            logger.info("User '{}' processed with {} valid roles", record.getUsername(), roles.size());
            } else {
                logger.warn("User '{}' has no valid roles to assign, user was created/updated but without any roles",
                           record.getUsername());
                userrepository.save(user);
            }
            
            user.setRoles(roles);
            userrepository.save(user);
        }
    }

  @Override
  @Transactional
  public List < User > findAll() {
    return userrepository.findAll();
  }

}