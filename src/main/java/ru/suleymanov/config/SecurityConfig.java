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
                        .requestMatchers("/", "/registration", "/error").permitAll()             // url доступны всем пользователям
                        .requestMatchers("/css/**").permitAll()                                                  // доступ для стилей css,js,image
                        .requestMatchers("/account/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())  // url доступны для user
                        .requestMatchers("/admin/**").hasRole("ADMIN")                                           // url доступны для админа
                        .anyRequest().authenticated()

                )
                // Авторизация и аутентификация
                .formLogin(form -> form
                        .loginPage("/login")                                                                        // кастомная страница логина
                        .loginProcessingUrl("/login")                                                               // URL, на который форма отправляет логин/пароль
                        .usernameParameter("email")                                                                 // здесь указан имя поля логина
                        .passwordParameter("password")                                                              // здесь указан имя поля для пароля
                        .defaultSuccessUrl("/account", true)                               // куда перенаправлять после успешного входа
                        .failureUrl("/error")                                                    // куда перенаправлять при ошибке
                        .permitAll()
                )
                // Выход из аккаунта
                .logout(logout -> logout
                        .logoutUrl("/logout")                                                                       // URL, по которому будет выход
                        .logoutSuccessUrl("/login")                                                                 // куда редирект после выхода
                        .invalidateHttpSession(true)                                                                // инвалидирует сессию
                        .deleteCookies("JSESSIONID")                                              // удаляет куки сессии
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
