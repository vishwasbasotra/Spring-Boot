package com.ecommerce.project.model;

import com.ecommerce.project.enums.AppRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleID")
    private Long roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "roleName", length = 20)
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
