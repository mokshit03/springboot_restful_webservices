package net.javaguides.springboot.service;
import java.util.List;
import net.javaguides.springboot.entity.Roles;

public interface RoleService {

Roles createRole(Roles userName); //CREATE NEW ROLES

List<Roles> getAllRoles(); //GET ALL THE APPLICATION ROLES
}
