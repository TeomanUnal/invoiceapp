package com.teoman.service;

import com.teoman.dto.DtoUser;
import com.teoman.dto.RegisterRequest;
import com.teoman.jwt.AuthRequest;
import com.teoman.jwt.AuthResponse;

public interface AuthService {

    public DtoUser register(RegisterRequest request);

    public AuthResponse authenticate(AuthRequest request);

}
