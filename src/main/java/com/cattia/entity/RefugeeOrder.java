package com.cattia.entity;

import com.cattia.model.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_REFUGEE_ORDER")
public class RefugeeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ID_USER", nullable = false)
    private UserAccount userAccount;

    @Column
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @OneToMany(mappedBy = "refugeeOrder", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REFUGEE", nullable = false)
    private Refugee refugee;
}
