package net.javaguides.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;

import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.UserRepository;


public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userepository.getUserByUsername(username);
        if(user==null)
        {throw new UsernameNotFoundException(username);}
        
        return new MyUserDetails(user);
       }
}
