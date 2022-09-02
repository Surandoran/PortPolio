package com.example.portfolio.config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser implements UserDetails {
    private Collection<? extends GrantedAuthority> authorities;
    private String email;  // principal - biz name : id
    private String pw;  // credential

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE
        return authorities;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return email;
    }


    // -------------- 계정에 대한 디테일한 설정 -----------------
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    // -----------------------------------------------

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.email = name;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public void setPassword(String pw) {
        this.pw = pw;
    }
}
