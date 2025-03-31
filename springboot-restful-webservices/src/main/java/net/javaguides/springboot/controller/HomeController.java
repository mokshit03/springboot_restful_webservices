package net.javaguides.springboot.controller;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> getSessionInfo(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            
            return ResponseEntity.ok("Hello - -      " + username + "- - welcome to the sailpoint training");
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "User not authenticated");
    }

    @GetMapping("/error")
    public String handleError() {
        return "Access Denied"; 
    }

    @PostMapping("/error")
    public String handlePostError() {
        return "Access Denied";
    }
}