package com.spring.Secure.Repo;

import com.spring.Secure.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByPasswordResetToken(String passwordResetToken);
}
