package com.dassda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
@CrossOrigin
public class FeController {
    //
    @GetMapping(value = "/")
    public String fePage() {
        return "index";
    }
}
