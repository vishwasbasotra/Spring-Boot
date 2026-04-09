package com.socialMedia.service;

import com.socialMedia.model.SocialUser;
import com.socialMedia.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    private SocialUserRepository socialUserRepository;


    public List<SocialUser> getAllUsers(){
        return socialUserRepository.findAll();
    }

    public SocialUser createUser(SocialUser socialUser) {
        if (socialUser.getSocialProfile() != null) {
            socialUser.getSocialProfile().setUser(socialUser);
        }
        return socialUserRepository.save(socialUser);
    }

    public SocialUser deleteUser(Long userId) {
        SocialUser socialUser = socialUserRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!!!"));
        socialUserRepository.delete(socialUser);
        return socialUser;
    }
}
