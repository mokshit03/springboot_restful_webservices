package net.javaguides.springboot.controller;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.ModifyNewRole;
import net.javaguides.springboot.entity.Pass;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Myapp/api/v1")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user){
        if(user.getStatus()==null)
        {
            user.setStatus("active");
        }
        User saveduser = userservice.createUser(user);
        return ResponseEntity.ok("Hello -"+saveduser.getUsername()+"!"+"Welcome to Deloitte.");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userservice.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{userId}/roles")
    public List<Roles> getUserRoles(@PathVariable Long userId)
    {
        return userservice.getUserRoles(userId);
    }

    @PostMapping("/admin/{userId}/roles")
    public String modifyUserRole(@PathVariable long userId, @RequestBody ModifyNewRole newroles) {
        
        return userservice.modifyUserRole(userId, newroles.getRoleName(), newroles.getAction());
    }

    @GetMapping("/users/{userId}")
    public Optional<User> getUser(@PathVariable Long userId)
    {
        return userservice.getUser(userId);
    }

    @PatchMapping("/users/{userId}/enable")
    public String enableUser(@PathVariable Long userId) {
    userservice.updateUserStatus(userId, "active");
    return "User enabled successfully!";
}
 
    @PatchMapping("/users/{userId}/disable")
    public String disableUser(@PathVariable Long userId) {
    userservice.updateUserStatus(userId, "Inactive");
    return "User disabled successfully!";
}
    
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody Pass pass) {
            ResponseEntity<String> response = userservice.changePassword(userId, pass.getOldPassword(), pass.getNewPassword());
            return response;
    }

        // @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    // public ResponseEntity<Integer> uploadUsers(
    //         @RequestPart("file")MultipartFile file
    //         ) throws IOException {
    //     return ResponseEntity.ok(userservice.uploadUsers(file));
    // }
    // @GetMapping("/error")
    //     public String handleError(HttpServletRequest request, Model model) {
    //         Object status = request.getAttribute("javax.servlet.error.status_code");
    //         if (status != null) {
    //             int statusCode = Integer.parseInt(status.toString());
    //             if (statusCode == HttpStatus.NOT_FOUND.value()) {
    //                 return "404";
    //             }
    //         }
    //                     return "error";
    // }
}
