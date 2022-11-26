package com.cattia.model;

import lombok.*;


import javax.persistence.*;

@Entity
@Table(name = "T_USERDATA")
@Getter
@Setter
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;
    private String email;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ACCOUNT_ID", nullable = false)
    private UserAccount userAccount;
}

