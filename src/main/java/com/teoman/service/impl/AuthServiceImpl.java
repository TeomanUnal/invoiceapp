package com.teoman.service.impl;

import com.teoman.dto.DtoUser;
import com.teoman.dto.RegisterRequest;
import com.teoman.jwt.AuthRequest;
import com.teoman.jwt.AuthResponse;
import com.teoman.jwt.JwtService;
import com.teoman.model.User;
import com.teoman.model.UserAuth;
import com.teoman.repository.UserAuthRepository;
import com.teoman.repository.UserRepository;
import com.teoman.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final UserAuthRepository userAuthRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationProvider authenticationProvider;

    private final JwtService jwtService;

    @Override
    @Transactional
    public DtoUser register(RegisterRequest request) {
        log.info("Kullanıcı kaydı başlatıldı: {}", request.getUsername());
        // 1) Kimlik
        UserAuth auth = new UserAuth();
        auth.setUsername(request.getUsername());
        auth.setPassword(passwordEncoder.encode(request.getPassword()));
        auth = userAuthRepository.save(auth);

        log.debug("UserAuth kaydedildi: {}", auth.getId());

        // 2) Profil
        User profile = User.builder()
                           .firstName(request.getFirstName())
                           .lastName(request.getLastName())
                           .email(request.getEmail())
                           .auth(auth)
                           .build();
        userRepository.save(profile);

        log.info("Kullanıcı profili oluşturuldu: {}", profile.getEmail());


        // 3) Geri dönüş DTO (parolayı asla dönme)
        DtoUser dto = new DtoUser();
        dto.setUsername(auth.getUsername());
        dto.setPassword(null);
        return dto;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {

        log.info("Giriş işlemi başlatıldı: {}", request.getUsername());

        var tokenReq = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        var authentication = authenticationManager.authenticate(tokenReq);

        log.info("Kimlik doğrulama başarılı: {}", request.getUsername());

        var ud = (org.springframework.security.core.userdetails.UserDetails)
                authentication.getPrincipal();

        String token = jwtService.generateToken(ud);

        log.info("JWT token üretildi");

        return new AuthResponse(token);
    }
}

