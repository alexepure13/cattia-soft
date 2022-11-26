package com.cattia.security;

import com.cattia.model.Permission;
import com.cattia.model.UserRole;
import com.cattia.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserAccountDetail implements UserDetails {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String fullName;

    private boolean active;

    private UserRole userRole;

    private List<GrantedAuthority> authorities;

    public UserAccountDetail(UserAccount userAccount) {
        this.username = userAccount.getUsername();
        this.password = userAccount.getPassword();
        this.firstName = userAccount.getFirstName();
        this.lastName = userAccount.getLastName();
        this.fullName = userAccount.getFirstName() + " " + userAccount.getLastName();
        if (userAccount.getActive() == 1){
            this.active = true;
        }else{
            this.active = false;
        }
        this.userRole = userAccount.getRole();
        this.authorities = userAccount.getRole().getPermissionList().stream()
                .map(Permission::getPermission)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return userRole.getName();
    }


}