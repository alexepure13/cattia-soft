package com.cattia.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_REFUGEE")
public class Refugee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private int active;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String identificationNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "refugee", fetch = FetchType.LAZY)
    private List<RefugeeOrder> orderList;

}
