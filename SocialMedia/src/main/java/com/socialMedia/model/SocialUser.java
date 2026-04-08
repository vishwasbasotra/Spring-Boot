package com.socialMedia.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "Social_Profile(FK)")
    private SocialProfile socialProfile;

    @OneToMany(mappedBy = "socialUser")
    private List<Post> posts = new ArrayList<>();
}
