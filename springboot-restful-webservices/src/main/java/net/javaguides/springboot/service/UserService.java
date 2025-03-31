package net.javaguides.springboot.service;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.entity.UserCsvRepresentation;

public interface UserService 
{

User createUser(User user); //CREATE USER/LOGIN

List<User> getAllUsers(); //LIST ALL THE USER DETAILS

List<Roles> getUserRoles(Long id); // ROLES BELONGING TO USER
 
String modifyUserRole(Long userId, String roleName, String action); // ASSIGN/REMOVE ROLES

Optional<User> getUser(Long id); // GET USER DETAILS

ResponseEntity<String> changePassword(Long id, String oldPassword, String newPassword ); // CHANGE PASSWORD

int updateUserStatus(Long id, String status);

List < User > findAll();

void processUserRecords(List<UserCsvRepresentation> records);

ResponseEntity<String> loginUser(String username, String password);
}
