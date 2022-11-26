package com.cattia.dto;

import com.cattia.entity.ProductCategoryEnum;
import com.cattia.entity.UnitOfMeasurementEnum;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFormDto {

    private Long id;

    @Pattern(regexp = "^\\d{5}$", message = "Product code must contain exactly 5 digits")
    private String code;

    @Size(min = 3, max = 50, message = "The product must have between {min} and {max} characters")
    private String name;

    @Valid
    private ProductCategoryEnum category;

    @Valid
    private UnitOfMeasurementEnum unitOfMeasurement;
}
