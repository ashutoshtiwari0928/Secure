package com.spring.Secure.Handler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoginFailureHandler
        implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {
        System.out.println("LoginFailureHandler");
        String username = request.getParameter("username");

        log.warn("Failed login attempt for user: {}",
                username);

        log.warn("Reason: {}",
                exception.getMessage());

        response.sendRedirect("/login?error");
    }
}