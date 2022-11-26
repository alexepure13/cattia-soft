package com.cattia.dto;

import com.cattia.entity.StockHistoryActionEnum;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockHistoryDto {

    private Long id;
    private Long productId;
    private String createdOn;
    private String createdBy;
    private StockHistoryActionEnum action;
    private BigDecimal quantity;
}
