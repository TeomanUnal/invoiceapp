package com.teoman.jwt;

//Her http isteğinde tokenı okuyup filtreleyip doğrulama yapar

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header;
        String token;
        String username;

        header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("Authorization header bulunamadı veya 'Bearer' ile başlamıyor.");
            filterChain.doFilter(request, response);
            return;
        }
        token = header.substring(7);
        try {
            username = jwtService.getUsernameByToken(token);
            log.debug("Token'dan kullanıcı adı alındı: {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Kullanıcı doğrulandı ve SecurityContext'e eklendi: {}", username);
                }
            }

        } catch (ExpiredJwtException e) {
            System.out.println("Token süresi dolmuştur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Genel bir hata oluştu: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
