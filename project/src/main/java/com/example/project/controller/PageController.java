package com.example.project.controller;

import com.example.project.model.Tweet;
import com.example.project.model.User;
import com.example.project.service.TweetService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class PageController {

    private final UserService userService;
    private final TweetService tweetService;
    @Autowired
    public PageController(UserService userService, TweetService tweetService) {
        this.userService = userService;
        this.tweetService = tweetService;
    }




    @GetMapping("/page")
    String page(Model model){
        User userFromDB = userService.findByUsername(userService.getAuthUsername());
        String fio = userFromDB.getName() +" " + userFromDB.getSurname();
        model.addAttribute("fio", fio); //получаем авторизованного пользователя и внедряем на страницу
        List<Tweet> tweets = tweetService.getAllTweet(userFromDB.getId());
        model.addAttribute("tweets", tweets);
        userFromDB.setPassword(null);
        model.addAttribute("user", userFromDB);
        return "page";
    }

    @PostMapping("/page")
    String pageTweetForm(String tweet){
        User userFromDB = userService.findByUsername(userService.getAuthUsername()); //поиск по авторизованному пользователю
        Tweet tweet1 = new Tweet(tweet, userFromDB);
        tweetService.addTweet(tweet1);
        return "redirect:/page";
    }

    @GetMapping("/inDeveloping")
    String inDeveloping(){
        return "inDeveloping";
    }
}
