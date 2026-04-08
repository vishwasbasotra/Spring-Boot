package com.socialMedia.service;

import com.socialMedia.model.SocialUser;
import com.socialMedia.repository.SocialUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    private SocialUserRepository socialUserRepository;


    public List<SocialUser> getAllUsers(){
        return socialUserRepository.findAll();
    }

    public SocialUser createUser(SocialUser socialUser) {
        return socialUserRepository.save(socialUser);
    }
}
