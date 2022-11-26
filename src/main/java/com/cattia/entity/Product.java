package com.cattia.entity;

import com.cattia.model.UserAccount;
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
@Table(name = "T_PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private int active;

    @Column
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ID_USER", nullable = false)
    private UserAccount userAccount;

    @Column
    private String code;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategoryEnum category;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasurementEnum unitOfMeasurement;

    @OneToMany(mappedBy = "product", cascade=CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<StockHistory> stockHistoryList;

    @OneToMany(mappedBy = "product", cascade=CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

}
