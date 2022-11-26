package com.cattia.mapper;

import com.cattia.dto.RefugeeFormDto;
import com.cattia.dto.RefugeeOverviewDto;
import com.cattia.dto.StatusEnum;
import com.cattia.entity.Refugee;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RefugeeMapper {

    private final Util util;

    public List<RefugeeOverviewDto> mapRefugeeOverviewDtoList(List<Refugee> refugeeList) {
        return refugeeList.stream()
                .map(this::mapRefugeeOverviewDto)
                .collect(Collectors.toList());
    }

    private RefugeeOverviewDto mapRefugeeOverviewDto(Refugee refugee) {
        return RefugeeOverviewDto.builder()
                .id(refugee.getId())
                .identificationNumber(refugee.getIdentificationNumber())
                .status(util.getStatus(refugee.getActive()))
                .fullName(refugee.getLastName() + " " + refugee.getFirstName())
                .build();
    }

    public RefugeeFormDto mapRefugeeFormDto(Refugee refugee) {
        return RefugeeFormDto.builder()
                .id(refugee.getId())
                .identificationNumber(refugee.getIdentificationNumber())
                .firstName(refugee.getFirstName())
                .lastName(refugee.getLastName())
                .build();
    }

    public Refugee mapNewRefugee(RefugeeFormDto refugeeDto) {
        return Refugee.builder()
                .id(refugeeDto.getId())
                .identificationNumber(refugeeDto.getIdentificationNumber())
                .active(StatusEnum.ACTIVE.getId())
                .createdOn(LocalDateTime.now())
                .firstName(refugeeDto.getFirstName())
                .lastName(refugeeDto.getLastName())
                .build();
    }

}
