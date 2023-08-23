package com.mohim.api.service;

import com.mohim.api.domain.Auth;
import com.mohim.api.domain.Role;
import com.mohim.api.domain.RolePrevilegeAssociation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    final private AuthService authService;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Auth auth = authService.getUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found. email=" + email));

        auth.setAuthorities(
                Stream.concat(
                        getRoles(auth.getRoles()).stream(),
                        getPrivileges(auth.getRoles()).stream()
                ).collect(Collectors.toList())
        );

        return auth;
    }

    private List<SimpleGrantedAuthority> getRoles(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> getPrivileges(List<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getRolePrevilegeAssociations().stream())
                .map(RolePrevilegeAssociation::getPrevilege)
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }
}
