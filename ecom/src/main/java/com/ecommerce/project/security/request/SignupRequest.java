package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 2, max = 30, message = "Please enter a valid name")
    private String username;

    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank
    @Size(min = 12)
    private String password;

    private Set<String> role;
}
