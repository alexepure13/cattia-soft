package com.cattia.util;


import com.cattia.dto.UserDto;
import com.cattia.model.UserAccount;

public final class UserEntityToDtoConverter {

    private UserEntityToDtoConverter() {
    }

    public static UserDto convertUserEntityToDto(final UserAccount user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

}
