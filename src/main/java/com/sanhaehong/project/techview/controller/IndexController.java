package com.sanhaehong.project.techview.controller;

import com.sanhaehong.project.techview.annotation.LogInUser;
import com.sanhaehong.project.techview.security.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
