package com.dassda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@CrossOrigin
public class FeController {
    @GetMapping()
    public RedirectView fePage() {
        return new RedirectView("https://www.dassda.today");
    }
//    @GetMapping()
//    public String pageFe() {
//        return "index";
//    }
}
