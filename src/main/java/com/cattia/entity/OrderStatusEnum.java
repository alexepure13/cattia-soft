package com.cattia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusEnum {

    OPEN("Open"),
    COMPLETED("Completed");

    private String value;
}
