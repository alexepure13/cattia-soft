package com.cattia.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefugeeOrderDto {

    private Long id;
    private Long refugeeId;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private OrderDto openOrder;
    private List<OrderDto> closedProductOrderList;
}
