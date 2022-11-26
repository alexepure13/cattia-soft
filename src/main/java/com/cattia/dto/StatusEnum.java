package com.cattia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {

    INACTIVE(0, "Inactive", "bg-danger"),
    ACTIVE(1, "Active", "bg-success"),
    RESIGNED(2, "Resigned", "bg-info");

    private int id;
    private String value;
    private String cssClass;

}
