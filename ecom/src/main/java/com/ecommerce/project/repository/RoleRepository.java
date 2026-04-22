package com.ecommerce.project.repository;

import com.ecommerce.project.enums.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
