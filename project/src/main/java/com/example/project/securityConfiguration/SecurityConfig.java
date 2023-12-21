
package com.example.project.securityConfiguration;

import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() // - запросы по этим путям доступны всем
                        .requestMatchers("/registration").permitAll()

                        .requestMatchers("/page").authenticated()// - запросы доступны только авторизованным пользователям
                        .requestMatchers("/inDeveloping").authenticated()

                        .requestMatchers("/administration").hasRole("ADMIN") // - запросы доступны только администраторам
                        .requestMatchers("/createUser").hasRole("ADMIN")
                        .requestMatchers("/updateUser").hasRole("ADMIN")
                        .requestMatchers("/deleteUser").hasRole("ADMIN")

                        .anyRequest().permitAll()
                ).csrf(AbstractHttpConfigurer::disable) // - отключаем защиту csrf

                .formLogin(form -> form // - настраиваем собственную страницу с авторизацией и регистрацией(по умолчанию регистрации нет)
                        .loginPage("/")
                        .loginProcessingUrl("/")
                        .failureUrl("/logout")
                        .defaultSuccessUrl("/page")
                )
                .logout(logout -> logout // - настраиваем выход с "аккаунта", очищаем сессии, куки
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    //Используется для аутентификации имени пользователя и пароля.
    //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    // Предоставляем спрингу объект для шифрования пароля
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

















}