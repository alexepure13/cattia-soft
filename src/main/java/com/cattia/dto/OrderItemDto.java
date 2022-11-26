package com.cattia.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;
    private Long orderId;
    private Long productId;
    private String code;
    private String name;
    private String category;
    private BigDecimal quantity;
    private BigDecimal availableQuantity;
    private String unitOfMeasurement;
}
