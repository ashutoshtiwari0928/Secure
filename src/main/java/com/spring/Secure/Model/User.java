package com.spring.Secure.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserTable")
@ToString
public class User {
    @Id
    private String username;
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String role;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiresAt;
}
