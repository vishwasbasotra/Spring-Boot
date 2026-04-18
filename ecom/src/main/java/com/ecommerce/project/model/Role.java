package com.ecommerce.project.model;

import com.ecommerce.project.enums.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "roleName", length = 20)
    private AppRole roleName;

}
