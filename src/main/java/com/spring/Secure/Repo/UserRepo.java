package com.spring.Secure.Repo;

import com.spring.Secure.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
