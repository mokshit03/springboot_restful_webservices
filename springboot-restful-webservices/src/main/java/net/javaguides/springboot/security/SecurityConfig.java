package net.javaguides.springboot.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import java.util.List;

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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/Mokshit_TargetApp_Sailpoint/protocol/openid-connect/certs").build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
            .clientId("Ov23liDaSyclhXoqbTN5") // Fill in your GitHub client ID
            .clientSecret("24c870810d14cbd7bc4a7bebc0a8a3fad1e34a67") // Fill in your GitHub client secret
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationUri("https://github.com/login/oauth/authorize")
            .tokenUri("https://github.com/login/oauth/access_token")
            .userInfoUri("https://api.github.com/user")
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .userNameAttributeName("id")
            .clientName("GitHub")
            .scope("read:user", "user:email") // Adding scope here
            .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

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
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:4200")); // Replace with your frontend URL
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .httpBasic(Customizer.withDefaults())
                // .formLogin(login -> login
                //         //.loginPage().permitAll()
                //         .successHandler((request, response, authentication) -> {
                //             HttpSession session = request.getSession();
                //             session.setAttribute("username", authentication.getName());
                //         })
                //         .defaultSuccessUrl("/", true)
                //         .failureForwardUrl("/error"))
                .logout(logout -> logout.permitAll())
                .exceptionHandling(eh -> eh
                       .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                       .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")))
                       .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}