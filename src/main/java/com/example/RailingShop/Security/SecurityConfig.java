package com.example.RailingShop.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/","/shop/index","/shop/login","/static/**","/resources/**","/error/**").permitAll()
                        .requestMatchers("/shop/register","/shop/products","/shop/home","/shop/all","/shop/admins"
                        ,"/shop/accountDetails","/shop/addOrder","/shop/addProduct","/shop/editProduct","/shop/shCart","/shop/orders",
                                "/shop/orderDetails","/shop/address_up").permitAll()
//                        .requestMatchers("/shop/addProduct","/shop/editProduct/*","/shop/editProduct/update/*","/shop/delete/*","/shop/all","/shop/addOrder/*","/order/addOrder","/order/orderMessage","/order/add").hasAnyAuthority("ADMIN","ROLE_ADMIN","USER","ROLE_USER")
//                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .anyRequest().hasAnyAuthority("ADMIN")
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/shop/home",true)
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                );

        return http.build();
    }
}
