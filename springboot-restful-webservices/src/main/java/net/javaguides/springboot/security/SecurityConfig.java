package net.javaguides.springboot.security;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    @Value("${cors.allowed.methods}")
    private String[] allowedMethods;

    @Value("${cors.allowed.headers}")
    private String[] allowedHeaders;

    @Value("${cors.allow.credentials}")
    private boolean allowCredentials;

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

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
            .requestMatchers("/Myapp/api/v1/login").permitAll()
            .requestMatchers("/Myapp/api/v1/users/{userId}/enable").hasAnyRole("USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/users/{userId}/disable").hasAnyRole("USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/users").hasAnyRole("USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/roles").hasAnyRole("USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/users/{userid}/password").hasAnyRole("USER","USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/users/{userid}/roles").hasAnyRole("USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/users/{userid}").hasAnyRole("USER","USER_MANAGER", "ADMIN", "SERVICE_ADMIN")
            .requestMatchers("/Myapp/api/v1/admin/**").hasAnyRole("SERVICE_ADMIN", "ADMIN")
            .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.setAllowedOrigins(List.of(allowedOrigins));
                corsConfig.setAllowedMethods(List.of(allowedMethods));
                corsConfig.setAllowedHeaders(List.of(allowedHeaders));
                corsConfig.setAllowCredentials(allowCredentials);
                return corsConfig;
            }))
            .httpBasic(Customizer.withDefaults())
            .logout(logout -> logout.permitAll())
            .exceptionHandling(eh -> eh
                    .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                    .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }
}