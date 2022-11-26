package com.cattia.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountEditFormDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 20, message = "Username must have between {min} and {max} characters")
    private String username;

    @NotNull
    @Size(min = 2, max = 30, message = "First Name must have between {min} and {max} characters")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30, message = "Last Name must have between {min} and {max} characters")
    private String lastName;

    @NotNull
    @Email(message = "Email address is not valid", regexp = "^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")
    private String email;

    private UserRoleDto role;
}
