package com.mohim.api.service;

import com.mohim.api.domain.Auth;
import com.mohim.api.domain.AuthRoleAssociation;
import com.mohim.api.domain.Role;
import com.mohim.api.dto.*;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.AuthRepository;
import com.mohim.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public void join(AuthJoinRequest authJoinRequest) {
        Auth auth = Auth.createAuth(authJoinRequest.getEmail(), passwordEncoder.encode(authJoinRequest.getPassword()));
        authRepository.save(auth);
    }


    public Auth getUser(Long id) {
        return authRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found user with id =" + id));
    }

    public Optional<Auth> getUser(String email) {
        return authRepository.findByEmail(email);
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        Optional<Auth> auth = authRepository.findByEmail(request.getEmail());

        if(auth.isEmpty()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ID);
        }
        if(!passwordEncoder.matches(request.getPassword(), auth.get().getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(auth.get());

        AuthLoginResponse response = AuthLoginResponse.builder()
                .accessToken(accessToken)
                .email(auth.get().getEmail())
                .role(auth.get().getAuthRoleAssociations().stream()
                        .map(AuthRoleAssociation::getRole)
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .build();

        return response;

    }
}
