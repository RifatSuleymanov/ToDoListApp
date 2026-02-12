package ru.suleymanov.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ADMIN,
    USER,
    SUPER_ADMIN;


    /// ROLE_USER
    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}
