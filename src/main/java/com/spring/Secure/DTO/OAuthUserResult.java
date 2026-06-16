package com.spring.Secure.DTO;

import com.spring.Secure.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthUserResult {
    private User user;
    private String temporaryPassword;

    public boolean isNewUser() {
        return temporaryPassword != null;
    }
}
