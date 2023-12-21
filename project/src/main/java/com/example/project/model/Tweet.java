package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tweets")
@Data
public class Tweet {
    public Tweet() {
    }

    public Tweet(String tweet, User user) {
        
        this.tweet = tweet;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tweet")
    private String tweet;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", tweet='" + tweet + '\'' +
                ", user=" + user +
                '}';
    }
}
