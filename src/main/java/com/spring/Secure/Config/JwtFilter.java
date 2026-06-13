package com.spring.Secure.Config;

import com.spring.Secure.DTO.UserDTO;
import com.spring.Secure.Service.JwtService;
import com.spring.Secure.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserService userService;
    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private void setJwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("doFilterInternal");
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            System.out.println("Bearer token found");
            String jwtToken = header.substring(7);
            String userName = null;
            if (!jwtToken.isBlank()) {
                userName = jwtService.extractUserName(jwtToken);

            }
            if (userName != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                if(jwtService.validateToken(jwtToken)){

                    UsernamePasswordAuthenticationToken
                            usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userName,
                                    null,
                                    userService.loadUserByUsername(userName)
                                            .getAuthorities()
                            );
                    usernamePasswordAuthenticationToken
                            .setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request)
                            );
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
