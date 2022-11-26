package com.cattia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductCategoryEnum {

    PERISHABLE("Perishable"),
    UNPERISHABLE("Unperishable"),
    PERSONAL_HYGENE("Personal hygene");

    private String value;

}
