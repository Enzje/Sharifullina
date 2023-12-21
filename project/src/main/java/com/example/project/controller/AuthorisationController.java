package com.example.project.controller;

import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/")
public class AuthorisationController {
    private final UserService userService;
    @Autowired
    public AuthorisationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String singin(Model model){
        model.addAttribute("usOrPassError","");
        return "singin";
    }

    @PostMapping
    String singinForm(@RequestParam String username, @RequestParam String password, Model model){
        if (!userService.existsByUsername(username) || !userService.findByUsername(username).getPassword().equals(password)){
            model.addAttribute("usOrPassError", "Неверное имя пользователя или пароль");
            return "singin";
        }
        return "page";
    }


}
