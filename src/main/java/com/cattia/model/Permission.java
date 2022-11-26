package com.cattia.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "T_PERMISSION")
@Getter
@Setter

public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String permission;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ID_PERMISSION"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROLE"))
    private List<UserRole> userRoleList;
}
