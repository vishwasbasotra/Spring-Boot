package com.socialMedia.contoller;

import com.socialMedia.model.SocialUser;
import com.socialMedia.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @GetMapping("/social/users")
    public ResponseEntity<List<SocialUser>> getUsers(){
        return new ResponseEntity<>(socialService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/social/users")
    public ResponseEntity<SocialUser> createUser(@RequestBody SocialUser socialUser){
        return new ResponseEntity<>(socialService.createUser(socialUser), HttpStatus.CREATED);
    }
}
