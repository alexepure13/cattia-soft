package com.cattia.service;

import com.cattia.entity.UnitOfMeasurementEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UnitOfMeasurementService {

    public List<UnitOfMeasurementEnum> getAllActiveUnitsOfMeasurement() {
        return Arrays.asList(UnitOfMeasurementEnum.values());
    }
}
