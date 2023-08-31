package com.mohim.api.controller;

import com.mohim.api.dto.AuthJoinRequest;
import com.mohim.api.dto.AuthLoginRequest;
import com.mohim.api.dto.AuthLoginResponse;
import com.mohim.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/users")
    public ResponseEntity<Void> join(@RequestBody AuthJoinRequest authJoinRequest) {

        authService.join(authJoinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {

        AuthLoginResponse response =authService.login(request);
        return ResponseEntity.ok().body(response);
    }
}
