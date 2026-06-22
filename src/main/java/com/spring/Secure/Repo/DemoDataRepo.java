package com.spring.Secure.Repo;

import com.spring.Secure.Model.DemoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoDataRepo extends JpaRepository<DemoData,String> {
}
