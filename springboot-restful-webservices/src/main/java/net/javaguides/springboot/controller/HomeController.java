package net.javaguides.springboot.controller;
import lombok.AllArgsConstructor;

// import java.util.HashMap;
// import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
// @RequestMapping("/Myapp/api/v1")
@AllArgsConstructor
public class HomeController {

    // private UserService userservice;
    // HttpSession session;

    @GetMapping("/")
    public ResponseEntity<String> getSessionInfo(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); // Get the logged-in username
            
        //    // Map<String, String> response = new HashMap<>();
        //     response.put("username", username);
           // String s ="Hello, welcome to the sailpoint training";
            return ResponseEntity.ok("Hello - -      " + username + "- - welcome to the sailpoint training");
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "User not authenticated");
    }

    @GetMapping("/error")
    public String handleError() {
        return "error"; // Return the name of the error view
    }
}