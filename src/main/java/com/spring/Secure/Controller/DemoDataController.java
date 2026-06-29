package com.spring.Secure.Controller;

import com.spring.Secure.Model.DemoData;
import com.spring.Secure.Repo.DemoDataRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
@Tag(name = "Demo apis for testing db operations.")
@SecurityRequirements(
        {
                @SecurityRequirement(name = "bearerAuth"),
                @SecurityRequirement(name = "oauth")
        }
)
public class DemoDataController {
    private DemoDataRepo demoDataRepo;
    @Autowired
    private void setDemoDataRepo(DemoDataRepo demoDataRepo) {
        this.demoDataRepo = demoDataRepo;
    }
    @GetMapping("/data")
    @Operation(summary = "Get data from DB")
    public List<DemoData> findAll() {
        return demoDataRepo.findAll();
    }
    @GetMapping("/post")
    @Operation(summary = "Sample db operation.")
    public void postData(){
        demoDataRepo.save(new DemoData("a","a@b.com"));
    }
}
