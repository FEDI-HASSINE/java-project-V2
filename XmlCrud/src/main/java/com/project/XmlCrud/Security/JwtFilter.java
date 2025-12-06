package com.project.XmlCrud.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            LOGGER.debug("Processing JWT from Authorization header");
            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getAllClaims(token);

                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                String cin = claims.get("cin", String.class);

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(role.toLowerCase());

                AuthenticatedUser principal = new AuthenticatedUser(cin, email, role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                        principal,
                                null,
                                Collections.singleton(authority)
                        );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                LOGGER.debug("Authenticated request for {} with role {}", email, role);
            } else {
                LOGGER.debug("JWT validation failed for token starting with {}", token.substring(0, Math.min(token.length(), 10)));
            }
        } else {
            LOGGER.trace("No Bearer token found on request {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
