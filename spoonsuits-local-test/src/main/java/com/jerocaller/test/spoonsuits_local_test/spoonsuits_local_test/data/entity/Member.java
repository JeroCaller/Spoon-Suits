package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {

    private String username;
    private String password;
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return authorities;
    }

}
