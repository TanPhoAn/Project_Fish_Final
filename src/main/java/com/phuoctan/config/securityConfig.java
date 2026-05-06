package com.phuoctan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class securityConfig {
    //bean cho quyền create cho ioc container spring
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/login").permitAll()
                        .requestMatchers("/register", "/register/save").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/contact","/contact/request-sending").permitAll()
                        .requestMatchers("/products/{categorySlug}").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/cv").permitAll()
                        //for testing only
//                        .requestMatchers("/home").permitAll()
                    .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin =  authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                            if(isAdmin){
                                response.sendRedirect("/admin/dashboard");
                            }else{
                                response.sendRedirect("/home");
                            }
                        }
                        )

                        .failureUrl("/login?error")
                        .permitAll()
                )
                 .logout(logout-> logout
                         .logoutUrl("/logout")
                         .logoutSuccessUrl("/login?logout")
                         .invalidateHttpSession(true)
                         .clearAuthentication(true)
                         .deleteCookies("JSESSIONID")

                 )
                 .sessionManagement(session -> session
                         .maximumSessions(1)
                         //session B log -> session a out
                         .maxSessionsPreventsLogin(false)
                         .expiredUrl("/login?expired"))
         ;
         return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
