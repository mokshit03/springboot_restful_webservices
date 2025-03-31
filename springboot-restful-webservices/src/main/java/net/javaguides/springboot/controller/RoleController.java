package net.javaguides.springboot.controller;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.Roles;
import net.javaguides.springboot.service.RoleService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/Myapp/api/v1")
@AllArgsConstructor
public class RoleController {

    @Autowired
    private RoleService roleservice;

    @PostMapping("/roles")
    public ResponseEntity<Roles> createRole(@RequestBody Roles role){

        Roles savedrole = roleservice.createRole(role);
        return new ResponseEntity<>(savedrole, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        List<Roles> roles = roleservice.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
