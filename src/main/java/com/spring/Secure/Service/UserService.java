package com.spring.Secure.Service;

import com.spring.Secure.DTO.UserDTO;
import com.spring.Secure.DTO.OAuthUserResult;
import com.spring.Secure.Model.User;
import com.spring.Secure.Model.UserDetailsImpl;
import com.spring.Secure.Repo.UserRepo;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
@Service
public class UserService implements UserDetailsService {
    private UserRepo userRepo;
    @Getter
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public void register(User user) {
        System.out.println("Register User");
        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
        userRepo.save(user);
    }

    public OAuthUserResult findOrCreateOAuthUser(String username, String email) {
        Optional<User> existingUser = userRepo.findById(username);
        if (existingUser.isPresent()) {
            return new OAuthUserResult(existingUser.get(), null);
        }

        String temporaryPassword = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        user.setRole("USER");
        return new OAuthUserResult(userRepo.save(user), temporaryPassword);
    }

    public String createPasswordResetToken(String username) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusMinutes(15));
        userRepo.save(user);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepo.findByPasswordResetToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid password reset token"));

        if (user.getPasswordResetTokenExpiresAt() == null
                || user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UsernameNotFoundException("Password reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        userRepo.save(user);
    }

    public UserDetails login(UserDTO userDto) throws UsernameNotFoundException {
        if(userDto==null){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        UserDetails userDetails = this.loadUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(),userDetails.getPassword())) {
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Bad credentials");
        }
    }

    @Override
    public UserDetails loadUserByUsername( String username)
            throws UsernameNotFoundException {
        Optional<User> user = userRepo.findById(username);
        if(user.isPresent()){
            return new UserDetailsImpl(user.get());
        }
        throw new UsernameNotFoundException("Username not found");
    }

}
