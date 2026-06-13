package com.spring.Secure.Service;

import com.spring.Secure.DTO.UserDTO;
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

import java.util.Objects;
import java.util.Optional;
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
