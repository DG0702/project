package com.project.project.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiFirstController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
