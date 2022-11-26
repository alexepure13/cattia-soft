package com.cattia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountFormDto extends UserAccountEditFormDto{

    @NotNull
    @Size(min = 8, max = 50, message = "Password must have between {min} and {max} characters")
    private String password;

    @NotNull
    @Size(min = 8, max = 50, message = "Password must have between {min} and {max} characters")
    private String confirmPassword;

    private StatusEnum status;

}
