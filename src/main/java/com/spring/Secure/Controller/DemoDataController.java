package com.spring.Secure.Controller;

import com.spring.Secure.Model.DemoData;
import com.spring.Secure.Repo.DemoDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoDataController {
    private DemoDataRepo demoDataRepo;
    @Autowired
    private void setDemoDataRepo(DemoDataRepo demoDataRepo) {
        this.demoDataRepo = demoDataRepo;
    }
    @GetMapping("/data")
    public List<DemoData> findAll() {
        return demoDataRepo.findAll();
    }
    @GetMapping("/post")
    public void postData(){
        demoDataRepo.save(new DemoData("a","a@b.com"));
    }
}
