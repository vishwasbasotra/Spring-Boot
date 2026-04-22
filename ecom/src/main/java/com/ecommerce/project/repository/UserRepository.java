package com.ecommerce.project.repository;

import com.ecommerce.project.model.User;
import com.ecommerce.project.security.services.UserDetailsImplementation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    boolean existsByUserName(@NotBlank @Size(min = 2, max = 30, message = "Please enter a valid name") String username);
}
