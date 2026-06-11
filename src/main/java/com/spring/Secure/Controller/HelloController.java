package com.spring.Secure.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public  String greet(){
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
