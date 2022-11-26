package com.cattia.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefugeeFormDto {

    private Long id;

    @Pattern(regexp = "^\\d{8}$", message = "Identification number must contain exactly 8 digits")
    private String identificationNumber;

    @Size(min = 3, max = 50, message = "The first name must have between {min} and {max} characters")
    private String firstName;

    @Size(min = 3, max = 50, message = "The last name must have between {min} and {max} characters")
    private String lastName;

}
