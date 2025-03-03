package net.javaguides.springboot.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
//@EnableWebSecurity
public class SecurityConfig{

    // @Bean
    // public UserDetailsService userDetailsService()
    // {
    //     UserDetails userOne = User.withUsername("user").password(passwordEncoder().encode("user1")).build();
    //     UserDetails adminOne = User.withUsername("admin").password(passwordEncoder().encode("admin1")).build();
        
    //     return new InMemoryUserDetailsManager(userOne, adminOne);
    // }


    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/Myapp/api/v1/users/***").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/Myapp/api/v1/users").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/Myapp/api/v1/roles").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/Myapp/api/v1/admin/**").hasRole( "ADMIN")
                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login.
                successHandler((request,response,authentication)->{
                    HttpSession session=request.getSession();
                    session.setAttribute("username", authentication.getName());
                }).
                defaultSuccessUrl("/", true).
                failureForwardUrl("/error").
                permitAll())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

}