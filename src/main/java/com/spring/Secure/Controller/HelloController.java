package com.spring.Secure.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public  String greet(Authentication authentication) {
        if(authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
            System.out.println(usernamePasswordAuthenticationToken);
        }else if(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken){
            System.out.println(oAuth2AuthenticationToken);
        }
        return "Hello World";
    }
    @GetMapping("/csrf")
    public Object csrf(HttpServletRequest request){
        return request.getAttribute("_csrf");
    }
    @GetMapping("/sessionid")
    public String sessionid(HttpServletRequest request){
        return request.getSession().getId();
    }
    @PostMapping
    public String post(HttpServletRequest request){
        return "Posting";
    }
}
