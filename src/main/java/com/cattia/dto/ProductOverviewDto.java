package com.cattia.dto;

import com.cattia.entity.ProductCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ProductOverviewDto {

    private Long id;
    private String code;
    private StatusEnum status;
    private ProductCategoryEnum category;
    private String name;
    private String unitOfMeasurement;
    private BigDecimal quantity;

}
