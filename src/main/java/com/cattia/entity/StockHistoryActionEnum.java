package com.cattia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StockHistoryActionEnum {

    ADD("Added"),
    REMOVED("Removed"),
    ORDERED("Ordered");

    private String value;
}
