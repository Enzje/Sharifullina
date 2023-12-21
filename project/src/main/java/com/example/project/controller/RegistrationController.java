package com.example.project.controller;

import com.example.project.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.project.service.UserService;
import com.example.project.service.UserValidateService;


@Controller
@RequestMapping("/")
public class RegistrationController {

    private final UserValidateService userValidateService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    public RegistrationController(UserValidateService userValidateService, UserService userService, BCryptPasswordEncoder encoder) {
        this.userValidateService = userValidateService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/registration")
    String registration(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }

    @PostMapping("/registration")
    String registrationForm(@Valid User user, BindingResult result){
        String message = userValidateService.CreateError(user);
        if(!message.isEmpty()){
            result.addError(new ObjectError("createError", message));
        }
        message = userValidateService.confirmPasswordError(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("confirmPasswordError", message));
        }
        message = userValidateService.trueUsername(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("trueUsername", message));
        }
        message = userValidateService.minLengthPassword(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("minLengthPassword", message));
        }
        if (result.hasErrors())
            return "registration";
        String encodingPass = encoder.encode(user.getPassword());
        user.setPassword(encodingPass);
        user.setRole("ROLE_USER");
        userService.saveUser(user);
        return "redirect:/";
    }
}
