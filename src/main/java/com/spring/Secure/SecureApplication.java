package com.spring.Secure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

@SpringBootApplication
public class SecureApplication {

	public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
	}

}
