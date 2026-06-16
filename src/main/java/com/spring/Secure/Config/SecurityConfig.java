package com.spring.Secure.Config;

import com.spring.Secure.Handler.OAuth2LoginSuccessHandler;
import com.spring.Secure.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private UserService userDetailsService;
    private JwtFilter jwtFilter;
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Autowired
    public void setOAuth2LoginSuccessHandler(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Autowired
    private void setUserDetailsService(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET, "/auth/login")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,
                                        "/auth/register")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,
                                        "/auth/forgot-password",
                                        "/auth/reset-password")
                                .permitAll()
                                .requestMatchers(
                                        "/oauth2/**",
                                        "/login/oauth2/**",
                                        "/login**")
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,"/sessionid"
                                )
                                .hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler))
                .sessionManagement(customizer
                        -> customizer
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )
                 .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(
                userDetailsService
        );
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
