package com.example.case_team_3.config;

import com.example.case_team_3.service.chat.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/setup-admin", "/register", "/login", "/css/**", "/js/**", "/error").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/cashier/**").hasRole("CASHIER")
//                .requestMatchers("/cleaner/**").hasRole("CLEANER")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .successHandler((request, response, authentication) -> {
//                    String role = authentication.getAuthorities().iterator().next().getAuthority();
//                    switch (role) {
//                        case "ROLE_ADMIN":
//                            response.sendRedirect("/admin/dashboard");
//                            break;
//                        case "ROLE_CASHIER":
//                            response.sendRedirect("/cashier");
//                            break;
//                        case "ROLE_CLEANER":
//                            response.sendRedirect("/cleanerhome");
//                            break;
//                        default:
//                            response.sendRedirect("/index");
//                    }
//                })
//                .failureUrl("/login?error=true")
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout=true")
//                .permitAll()
//            );
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/setup-admin", "/register", "/login", "/css/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/cashier/**").hasRole("CASHIER")
                        .requestMatchers("/cleaner/**").hasRole("CLEANER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            switch (role) {
                                case "ROLE_ADMIN":
                                    response.sendRedirect("/admin/dashboard");
                                    break;
                                case "ROLE_CASHIER":
                                    response.sendRedirect("/cashier/showCashierHome");
                                    break;
                                case "ROLE_CLEANER":
                                    response.sendRedirect("/cleaner/showCleanerHome");
                                    break;
                                case "ROLE_USER":
                                    response.sendRedirect("/user/userHome");
                                    break;
                                default:
                                    response.sendRedirect("/login");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}


