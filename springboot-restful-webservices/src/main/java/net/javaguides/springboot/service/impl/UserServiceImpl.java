package net.javaguides.springboot.service.impl;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userrepository;
    private RoleRepository rolerepository;

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
        //String username = authentication.getName();
 
        User user = userrepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
       // String encodedOld = passwordEncoder.encode(oldPassword);
        // Validate old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }
 
        // Encrypt new password
        String encryptedPassword = passwordEncoder.encode(newPassword);
 
        // Update in database
        user.setPassword(encryptedPassword);
        userrepository.save(user);
 
        return ResponseEntity.ok("Password updated successfully!");
    }


    @Override
    public void updateUserStatus(Long id, String status) {
        Optional<User> optuser=userrepository.findById(id);
        if (optuser.isPresent()) {
            User user = optuser.get();
            user.setStatus(status);            
            userrepository.save(user);
        }
    }

}
// userrepository.findAll()
// userrepository.deleteById()