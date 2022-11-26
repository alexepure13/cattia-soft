package com.cattia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefugeeOrderConfirmationDto {

    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private OrderDto completedOrder;

}
