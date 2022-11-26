package com.cattia.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefugeeOverviewDto {

    private Long id;
    private String identificationNumber;
    private StatusEnum status;
    private String fullName;

}
