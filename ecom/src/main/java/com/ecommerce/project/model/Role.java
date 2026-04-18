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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roleID")
    private Integer roleId;

    @ToString.Exclude
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "roleName", length = 20)
    private AppRole roleName;

}
