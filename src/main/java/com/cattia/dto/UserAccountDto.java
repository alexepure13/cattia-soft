package com.cattia.dto;

import com.cattia.model.UserAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountDto {

    private String personName;

    private String userName;

    private String role;

    public UserAccountDto(UserAccount userAccount) {
        this.personName = userAccount.getFirstName() + " " + userAccount.getLastName();
        this.userName = userAccount.getUsername();
        this.role = userAccount.getRole().getName();
    }
}
