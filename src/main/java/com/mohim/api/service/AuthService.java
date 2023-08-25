package com.mohim.api.service;

import com.mohim.api.domain.Auth;
import com.mohim.api.dto.*;
import com.mohim.api.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void join(AuthJoinRequest authJoinRequest) {
        Auth auth = Auth.createAuth(authJoinRequest.getEmail(), bCryptPasswordEncoder.encode(authJoinRequest.getPassword()));
        authRepository.save(auth);
    }


    public Auth getUser(Long id) {
        return authRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found user with id =" + id));
    }

    public Optional<Auth> getUser(String email) {
        return authRepository.findByEmail(email);
    }

}
