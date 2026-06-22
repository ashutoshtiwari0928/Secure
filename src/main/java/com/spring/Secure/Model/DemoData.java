package com.spring.Secure.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;
    public String name;
    public String email;

    public DemoData(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
