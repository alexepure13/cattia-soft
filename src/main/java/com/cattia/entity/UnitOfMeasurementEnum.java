package com.cattia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnitOfMeasurementEnum {

    PCS("pcs"),
    BAX("bax"),
    KG("kg"),
    GR100("100gr"),
    GR250("250gr"),
    GR500("500gr"),
    GR750("750gr"),
    LITRE("l"),
    ML100("100ml"),
    ML250("250ml"),
    ML500("500ml"),
    ML750("750ml");

    private String value;
}
