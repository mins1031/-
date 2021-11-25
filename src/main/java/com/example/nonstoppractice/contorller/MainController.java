package com.example.nonstoppractice.contorller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping()
    public ResponseEntity getMain() {
        String name = "Main";
        return ResponseEntity.ok().body(name);
    }
}
