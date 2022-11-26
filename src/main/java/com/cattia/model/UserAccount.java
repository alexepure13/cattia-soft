package com.cattia.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_USERACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 20, message = "Username must have between {min} and {max} characters")
    @Column(name = ("username"), unique = true)

    private String username;
    @NotNull
    @Size(min = 4, message = "Password must have more than {min} characters")
    @Column(name = "password")

    private String password;
    @NotNull
    @Size(min = 2, max = 30, message = "First Name must have between {min} and {max} characters")
    @Column(name = "firstname")

    private String firstName;
    @NotNull
    @Size(min = 2, max = 30, message = "Last Name must have between {min} and {max} characters")
    @Column(name = "lastname")
    private String lastName;

    @NotNull
    @Email(message = "Email address is not valid", regexp = "^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")
    @Column(name = "email", unique = true)
    private String email;

    @Column
    private int active;

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private UserRole role;

}