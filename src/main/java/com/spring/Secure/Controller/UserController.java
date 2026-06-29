package com.spring.Secure.Controller;
import com.spring.Secure.DTO.ForgotPasswordRequest;
import com.spring.Secure.DTO.ResetPasswordRequest;
import com.spring.Secure.DTO.UserDTO;
import com.spring.Secure.Model.User;
import com.spring.Secure.Service.JwtService;
import com.spring.Secure.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "User Service APIs.", description = "User registration, login, forgot password, reset password.")
public class UserController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/auth/register")
    @Operation(summary = "Register new user.", description = "Username should be unique.")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (UsernameNotFoundException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "Login using username and password.",
            description = "Username and password should " +
                    "match one of the credentials in the database."
    )
    public ResponseEntity<?> login(@RequestBody UserDTO userDto) {
        try {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDto.getUsername(),userDto.getPassword()
                        )
                );
                if(authentication.isAuthenticated()) {
                    return new ResponseEntity<>(jwtService.generateToken(userDto.getUsername()), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Failure",HttpStatus.UNAUTHORIZED);
                }

            }
            catch (UsernameNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/forgot-password")
    @Operation(summary = "Forgot password API.", description = "If user forgot password, he" +
            "can reset it using this api. Request should contain userid of the user registered. " +
            "For testing purpose the reset token is being returned as response. But, in real world" +
            "use case, this reset token is supposed to be emailed to user on his email address, so that " +
            "no one can change any other user's password, and user get notified if someone is trying to " +
            "change his/her password.")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String resetToken = userService.createPasswordResetToken(request.getUsername());
            return new ResponseEntity<>(resetToken, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/auth/reset-password")
    @Operation(summary = "Reset Password API.", description = "After hitting forgot password API, we have to hit this API for " +
            "resetting password. A reset token will be generated for the user after hitting " +
            "forgot password API. User needs to enter this in reset token and new password.")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
