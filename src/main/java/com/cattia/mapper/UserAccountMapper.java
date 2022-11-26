package com.cattia.mapper;

import com.cattia.dto.UserAccountFormDto;
import com.cattia.dto.UserAccountOverviewDto;
import com.cattia.dto.UserRoleDto;
import com.cattia.model.UserAccount;
import com.cattia.model.UserRole;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAccountMapper {

    private final Util util;

    public List<UserAccountOverviewDto> mapUserAccountOverviewDtoList(List<UserAccount> userAccountList) {
        return userAccountList.stream()
                .map(this::mapUserAccountOverviewDto)
                .collect(Collectors.toList());
    }

    private UserAccountOverviewDto mapUserAccountOverviewDto(UserAccount userAccount) {
        return UserAccountOverviewDto.builder()
                .id(userAccount.getId())
                .status(util.getStatus(userAccount.getActive()))
                .username(userAccount.getUsername())
                .email(userAccount.getEmail())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .role(userAccount.getRole().getName())
                .build();
    }


    public UserAccountFormDto mapUserAccountFormDto(UserAccount userAccount) {
        UserRoleDto userRole = getUserRoleDto(userAccount);

        UserAccountFormDto userAccountFormDto = new UserAccountFormDto();
        userAccountFormDto.setId(userAccount.getId());
        userAccountFormDto.setStatus(util.getStatus(userAccount.getActive()));
        userAccountFormDto.setUsername(userAccount.getUsername());
        userAccountFormDto.setEmail(userAccount.getEmail());
        userAccountFormDto.setFirstName(userAccount.getFirstName());
        userAccountFormDto.setLastName(userAccount.getLastName());
        userAccountFormDto.setRole(userRole);
        userAccountFormDto.setPassword(null);
        userAccountFormDto.setConfirmPassword(null);
        return userAccountFormDto;
    }

    private UserRoleDto getUserRoleDto(UserAccount userAccount) {
        return UserRoleDto.builder()
                .id(userAccount.getRole().getId())
                .role(userAccount.getRole().getRole())
                .name(userAccount.getRole().getName())
                .build();
    }

    public UserAccount mapUserAccount(UserAccountFormDto userAccount) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return UserAccount.builder()
                .id(userAccount.getId())
                .active(userAccount.getStatus().getId())
                .username(userAccount.getUsername())
                .email(userAccount.getEmail())
                .password(encoder.encode(userAccount.getPassword()))
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .role(getUserRole(userAccount))
                .build();
    }

    private UserRole getUserRole(UserAccountFormDto userAccount) {
        return UserRole.builder()
                .id(userAccount.getRole().getId())
                .build();
    }


}
