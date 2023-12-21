package com.example.project.service;

import com.example.project.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidateService {

    final private UserService userService;

    @Autowired
    public UserValidateService(UserService userService) {
        this.userService = userService;
    }




    public String CreateError(User user){
        String message = "";
        if(userService.existsByUsername(user.getUsername())){
            message = "Пользователь с таким именем уже зарегистрирован.";
        }
        return message;
    }

    public String confirmPasswordError(User user){
        String message="";
        if(!user.getPassword().equals(user.getConfirmPassword())){
            message="Не совпадают пароли.";
        }
        return message;
    }

    public String trueUsername(User user){
        String regex = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
        String message = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getUsername());
        if (!matcher.matches()){
            message = "Некорректный логин.";
        }
        return message;
    }
    public String minLengthPassword(User user){
        String message="";
        if (user.getPassword().length() < 4){
            message="Длина пароля минимум 4 символа.";
        }
        return message;
    }
    public String userIsNotAuth(User user){
        String message="";
        //добавить метод с авторизованным пользователем
        if (user.getUsername().equals(userService.getAuthUsername())){
            message = "Невозможно изменить логин авторизованного пользователя.";
        }
        return message;
    }
}
