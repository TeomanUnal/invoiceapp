package com.teoman.controller;

import com.teoman.dto.DtoUser;
import com.teoman.dto.RegisterRequest;
import com.teoman.jwt.AuthRequest;
import com.teoman.jwt.AuthResponse;
import com.teoman.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestAuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public DtoUser register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody @Valid AuthRequest request) {
        return authService.authenticate(request);
    }
}
