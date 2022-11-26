package com.cattia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountOverviewDto {


    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private StatusEnum status;

}
