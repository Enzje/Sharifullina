package com.example.project.controller;
import com.example.project.model.User;
import com.example.project.service.UserService;
import com.example.project.service.UserValidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/")
public class AdministrationController {
    private final UserService userService;
    private final UserValidateService userValidateService;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    public AdministrationController(UserService userService, UserValidateService userValidateService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.userValidateService = userValidateService;
        this.encoder = encoder;
    }

    @GetMapping("/administration")
    String administration(Model model){
        model.addAttribute("users", userService.getAllUsers());
        String name = userService.getAuthUsername();
        model.addAttribute("authUser", userService.getAuthUsername()); //Добавить получение авторизованного пользователя
        return "administration";
    }
    @GetMapping("/createUser")
    String createUser(Model model){
        model.addAttribute("user", new User());
        return "createUser";
    }
    @PostMapping("/createUser")
    String createUserForm(@Valid User user, BindingResult result){
        String message = userValidateService.CreateError(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("createError", message));
        }
        message = userValidateService.trueUsername(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("trueUsername",message));
        }
        message = userValidateService.minLengthPassword(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("minLengthPassword",message));
        }
        if (user.getRole() == null){
            result.addError(new ObjectError("nullRole", "Выберите роль."));
        }

        if (result.hasErrors()){
            return "createUser";
        }
        String encodingPass = encoder.encode(user.getPassword()); //доделать шифрование паролей
        user.setPassword(encodingPass);
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @GetMapping("/updateUser/{id}")
    String updateUser(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.findById(id));
        return "updateUser";
    }

    @PostMapping("/updateUser")
    String updateUserForm(@Valid User user, BindingResult result){

        User userFromDB = userService.findById(user.getId());
        String message ="";

        message = userValidateService.userIsNotAuth(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("userIsNotAuth", message));
        }

        if (!user.getUsername().equals(userFromDB.getUsername())){
            message = userValidateService.CreateError(user);
            if(!message.isEmpty()){
                result.addError(new ObjectError("createError", message));
            }
        }

        message = userValidateService.trueUsername(user);
        if (!message.isEmpty()){
            result.addError(new ObjectError("trueUsername", message));
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(userFromDB.getPassword());
        }else{
            message = userValidateService.minLengthPassword(user);
            if (!message.isEmpty()){
                result.addError(new ObjectError("minLengthPassword", message));
            }
        }

        if (result.hasErrors()){
            return "updateUser";
        }
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @GetMapping("/deleteUser/{id}")
    String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/administration";
    }
}
