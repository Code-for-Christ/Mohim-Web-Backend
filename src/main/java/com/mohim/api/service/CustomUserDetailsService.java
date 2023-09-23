package com.mohim.api.service;

import com.mohim.api.domain.Auth;
import com.mohim.api.domain.AuthRoleAssociation;
import com.mohim.api.domain.Role;
import com.mohim.api.domain.RolePermissionAssociation;
import com.mohim.api.repository.AuthRepository;
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
    final private AuthRepository authRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Auth auth = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found. email=" + email));

        auth.setAuthorities(
                Stream.concat(
                        getRoles(auth.getAuthRoleAssociations()).stream(),
                        getPrivileges(auth.getAuthRoleAssociations()).stream()
                ).collect(Collectors.toList())
        );

        return auth;
    }

    private List<SimpleGrantedAuthority> getRoles(List<AuthRoleAssociation> authRoleAssociations) {
        return authRoleAssociations.stream()
                .map(AuthRoleAssociation::getRole)
                .map(Role::getName)
                .map(role -> {
                    role = "ROLE_"+ role.toUpperCase();
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> getPrivileges(List<AuthRoleAssociation> authRoleAssociations) {
        return authRoleAssociations.stream()
                .map(AuthRoleAssociation::getRole)
                .flatMap(role -> role.getRolePermissionAssociations().stream())
                .map(RolePermissionAssociation::getPermission)
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }
}
