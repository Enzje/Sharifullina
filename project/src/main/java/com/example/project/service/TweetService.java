package com.example.project.service;

import com.example.project.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.repository.TweetRepository;

import java.util.Collections;
import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweet;
    @Autowired
    public TweetService(TweetRepository tweet) {
        this.tweet = tweet;
    }
    public void addTweet(Tweet tweet){
        this.tweet.save(tweet);
    }
    public List<Tweet> getAllTweet(Long id){
        return tweet.findAllByUser_id(id);
    }
}
