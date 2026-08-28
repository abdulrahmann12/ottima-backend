package com.ottima.finishing_tracking.security;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            String username = jwtService.extractUsername(token);

            if (username == null) {
                throw new AuthenticationCredentialsNotFoundException(Messages.BAD_CREDENTIALS);
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    // Use authorities from CustomUserDetails — already has ROLE_ prefix
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationCredentialsNotFoundException ex) {
            sendErrorResponse(response, Messages.UNAUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED, request);
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            sendErrorResponse(response, Messages.SESSION_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED, request);
        } catch (io.jsonwebtoken.JwtException ex) {
            sendErrorResponse(response, Messages.BAD_CREDENTIALS, HttpServletResponse.SC_UNAUTHORIZED, request);
        } catch (Exception ex) {
            sendErrorResponse(response, Messages.AUTH_FAILED, HttpServletResponse.SC_UNAUTHORIZED, request);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException(Messages.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        if (token.isBlank()) {
            throw new AuthenticationCredentialsNotFoundException(Messages.UNAUTHORIZED);
        }
        return token;
    }

    private void sendErrorResponse(HttpServletResponse response, String message,
                                   int status, HttpServletRequest request) throws IOException {
        BaseResponse error = new BaseResponse(message, request.getRequestURI());
        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), error);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(SecurityConstants.PUBLIC_PATHS)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}

