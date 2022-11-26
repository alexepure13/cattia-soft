package com.cattia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDto {

    private Long id;

    private String role;

    private String name;
}
