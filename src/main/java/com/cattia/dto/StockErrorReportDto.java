package com.cattia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockErrorReportDto {

    private Long refugeeId;
    private List<StockErrorMessageDto> stockErrorMessageList;
}
