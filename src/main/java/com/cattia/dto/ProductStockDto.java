package com.cattia.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDto {

    private Long id;
    private String code;
    private String name;
    private String category;
    private BigDecimal availableQuantity;
    private BigDecimal quantity;
    private String unitOfMeasurement;
    private List<ProductStockHistoryDto> productStockHistoryList;
}
