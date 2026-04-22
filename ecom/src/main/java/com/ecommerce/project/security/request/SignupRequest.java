package com.ecommerce.project.security.request;

import com.ecommerce.project.enums.AppRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    @Column(name = "username")
    @NotBlank
    @Size(min = 2, max = 30, message = "Please enter a valid name")
    private String username;

    @Column(name = "email")
    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank
    @Size(min = 12)
    @Column(name = "password")
    private String password;

    private Set<String> role;
}
