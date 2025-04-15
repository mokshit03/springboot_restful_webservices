package net.javaguides.springboot.controller;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.ModifyNewRole;
import net.javaguides.springboot.entity.Pass;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.entity.UserCsvRepresentation;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.service.impl.CsvParserService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/Myapp/api/v1")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userservice;
    @Autowired
    private CsvParserService csvParserService;

    @Autowired
    private UserRepository userrepository;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        ResponseEntity<User> response = userservice.loginUser(user.getUsername(), user.getPassword());
        return response;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user){
        if(user.getStatus()==null)
        {
            user.setStatus("active");
        }
        if(user.getPassword()==null)
        {
            user.setPassword("welcome@123");
        }
        User saveduser = userservice.createUser(user);
        return ResponseEntity.ok("Hello -"+saveduser.getUsername()+"!"+"Welcome to Deloitte.");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getPaginatedUsers(@RequestParam(defaultValue = "1") Integer page, 
    @RequestParam(defaultValue = "100000000") Integer limit){
            Page<User> pages=userservice.PaginatedUsers(page, limit);
            List<User> userslist = pages.getContent();
            return ResponseEntity.ok(userslist);
    }
    
    @GetMapping("/users/{userId}/roles")
    public List<Roles> getUserRoles(@PathVariable Long userId)
    {
        return userservice.getUserRoles(userId);
    }

    @PostMapping("/users/{userId}/roles")
    public String modifyUserRole(@PathVariable long userId, @RequestBody ModifyNewRole newroles) {
        
        return userservice.modifyUserRole(userId, newroles.getRoleName(), newroles.getAction());
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId)
    {
        return userservice.getUser(userId);
    }

    @PatchMapping("/users/{userId}/enable")
    public String enableUser(@PathVariable Long userId) {
        if(userrepository.findById(userId).isEmpty())
        {
            return "SORRY! NO SUCH USER EXISTS, Check the USERID!";
        }
        else{
    userservice.updateUserStatus(userId, "active");
    return "User enabled successfully!";
        }

}
 
    @PatchMapping("/users/{userId}/disable")
    public String disableUser(@PathVariable Long userId) {
    int i=userservice.updateUserStatus(userId, "Inactive");
    if(i==1)
    {
        return "SORRY! SUPER_ADMIN CAN NOT BE DISABLED!!";
    }
    if(userrepository.findById(userId).isEmpty())
    {
        return "SORRY! NO SUCH USER EXISTS, Check the USERID!";
    }
    else{
        return "User disabled successfully!";
    }
}
    
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody Pass pass) {
            ResponseEntity<String> response = userservice.changePassword(userId, pass.getOldPassword(), pass.getNewPassword());
            return response;
    }

    @PostMapping(value = "/admin/users/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            
            String filename = Objects.requireNonNull(file.getOriginalFilename());
            if (!filename.toLowerCase().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("Only CSV files are allowed");
            }
            
            List<UserCsvRepresentation> records = csvParserService.parseCsvFile(file);
            userservice.processUserRecords(records);
            
            return ResponseEntity.ok("File processed successfully: " + records.size() + " records imported");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to read file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to process file: " + e.getMessage());
        }
    }
}
