package ru.suleymanov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.suleymanov.entity.UserRole;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registration", "/error").permitAll()             // url доступны всем пользователям
                        .requestMatchers("/css/**").permitAll()                                                  // доступ для стилей css,js,image
                        .requestMatchers("/account/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())  // url доступны для user
                        .requestMatchers("/admin/**").hasRole("ADMIN")                                           // url доступны для админа
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")                                                                        // кастомная страница логина
                        .loginProcessingUrl("/login")                                                               // URL, на который форма отправляет логин/пароль
                        .defaultSuccessUrl("/account", true)                               // куда перенаправлять после успешного входа
                        .failureUrl("/error")                                                    // куда перенаправлять при ошибке
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
