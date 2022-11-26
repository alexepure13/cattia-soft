package com.cattia.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private String orderNumber;
    private String createdOn;
    private String createdBy;
    private String status;
    private List<OrderItemDto> orderedItems;

}
