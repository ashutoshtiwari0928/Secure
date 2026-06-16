package com.spring.Secure.Handler;

import com.spring.Secure.DTO.OAuthUserResult;
import com.spring.Secure.Model.User;
import com.spring.Secure.Service.JwtService;
import com.spring.Secure.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserService userService;

    public OAuth2LoginSuccessHandler(
            JwtService jwtService,
            UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        String provider = oauthToken.getAuthorizedClientRegistrationId();
        String email = oauthUser.getAttribute("email");
        String providerId = extractProviderId(oauthUser);
        String username = email != null ? email : provider + "_" + providerId;

        OAuthUserResult result = userService.findOrCreateOAuthUser(username, email);
        User user = result.getUser();
        String jwtToken = jwtService.generateToken(user.getUsername());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if (result.isNewUser()) {
            response.getWriter().write("""
                    {"token":"%s","username":"%s","email":"%s","role":"%s","temporaryPassword":"%s","message":"Save this temporary password now. It will not be shown again."}"""
                    .formatted(
                            escapeJson(jwtToken),
                            escapeJson(user.getUsername()),
                            escapeJson(user.getEmail()),
                            escapeJson(user.getRole()),
                            escapeJson(result.getTemporaryPassword())));
        } else {
            response.getWriter().write("""
                    {"token":"%s","username":"%s","email":"%s","role":"%s"}"""
                    .formatted(
                            escapeJson(jwtToken),
                            escapeJson(user.getUsername()),
                            escapeJson(user.getEmail()),
                            escapeJson(user.getRole())));
        }
    }

    private String extractProviderId(OAuth2User oauthUser) {
        Object sub = oauthUser.getAttribute("sub");
        if (sub != null) {
            return sub.toString();
        }

        Object id = oauthUser.getAttribute("id");
        if (id != null) {
            return id.toString();
        }

        Object login = oauthUser.getAttribute("login");
        if (login != null) {
            return login.toString();
        }

        return oauthUser.getName();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
