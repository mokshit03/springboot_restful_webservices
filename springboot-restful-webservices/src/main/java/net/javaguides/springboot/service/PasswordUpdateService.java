package net.javaguides.springboot.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.UserRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PasswordUpdateService {

    private static final Logger logger = Logger.getLogger(PasswordUpdateService.class.getName());

    private final UserRepository userrepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordUpdateService(UserRepository userRepository) {
        this.userrepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void updatePasswords() {
        List<User> users = userrepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$") && !user.getPassword().startsWith("$2y$")) {
                logger.info("Updating password for user: " + user.getUsername());
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userrepository.save(user);
            }
        }
    }
}