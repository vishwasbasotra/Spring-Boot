package com.socialMedia.model;

import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="User_ID")
    private SocialUser socialUser;
}
