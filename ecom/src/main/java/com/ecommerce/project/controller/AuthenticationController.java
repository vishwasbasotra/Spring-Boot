package com.ecommerce.project.controller;

import com.ecommerce.project.enums.AppRole;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthenticationController  {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect((Collectors.toList()));

        UserInfoResponse loginResponse = new UserInfoResponse(
                userDetails.getId(),
                jwtToken,
                userDetails.getUsername(),
                roles
        );
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest){
        if(userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: username is already taken!"));
        }
        if(userRepository.existsByUserName(signupRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: email is already taken!"));
        }
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roleSet = new HashSet<>();
        if(signupRequest.getRole() == null){
            Role role = roleRepository.findByRole(AppRole.USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found!"));
            roleSet.add(role);
        }else{
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByRole(AppRole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role is not found!"));
                        roleSet.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRole(AppRole.SELLER)
                                .orElseThrow(() -> new RuntimeException("Role is not found!"));
                        roleSet.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRole(AppRole.USER)
                                .orElseThrow(() -> new RuntimeException("Role is not found!"));
                        roleSet.add(userRole);
                }
            });
        }
        user.setRoles(roleSet);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
