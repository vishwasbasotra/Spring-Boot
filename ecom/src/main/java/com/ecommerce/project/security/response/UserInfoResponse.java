package com.ecommerce.project.security.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    @Getter
    @Setter
    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;
}
