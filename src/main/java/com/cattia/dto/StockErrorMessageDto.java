package com.cattia.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockErrorMessageDto {

    private String code;
    private String name;
    private String orderedQuantity;
    private String availableQuantity;

    public String toMessage() {
        return "Product " + this.code + " / '" + this.name + "': " +
                " Ordered quantity of " + this.orderedQuantity +
                " can't be greater than " + this.availableQuantity + ", available in stock.";

    }
}
