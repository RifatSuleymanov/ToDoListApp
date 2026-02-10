package ru.suleymanov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.UserRole;
import ru.suleymanov.repository.UserRepository;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/registration", "/error").permitAll()                         // url доступны всем пользователям
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
    //Идентификация пользователей
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository
                        .findByEmailIgnoreCase(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User with email = " + username + " not found!"));
                Set<SimpleGrantedAuthority> roles = Collections.singleton(user.getUserRole().toAuthority());
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
