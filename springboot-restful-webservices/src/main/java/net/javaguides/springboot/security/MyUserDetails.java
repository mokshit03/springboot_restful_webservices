package net.javaguides.springboot.security;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.entity.User;

@AllArgsConstructor
public class MyUserDetails implements UserDetails {

    private User user;

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     List<Roles> roles = user.getRoles();
    //     List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
    //     for (Roles role : roles) {
    //         authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    //     }
         
    //     return authorities;
    //     // throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    // }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
}
