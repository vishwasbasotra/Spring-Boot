package com.socialMedia.model;

import jakarta.persistence.*;

@Entity
public class SocialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "socialProfile")
    private SocialUser socialUser;
}
