package com.cattia.service;

import com.cattia.dto.RefugeeFormDto;
import com.cattia.dto.RefugeeOverviewDto;
import com.cattia.entity.Refugee;
import com.cattia.mapper.RefugeeMapper;
import com.cattia.repository.RefugeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RefugeeService {
    private final RefugeeRepository refugeeRepository;

    private final RefugeeMapper refugeeMapper;

    public List<RefugeeOverviewDto> getAllRefugees() {
        return refugeeMapper.mapRefugeeOverviewDtoList(refugeeRepository.findAll());
    }

    public List<RefugeeOverviewDto> getAllActiveRefugees() {
        return refugeeMapper.mapRefugeeOverviewDtoList(refugeeRepository.findAllActiveRefugees());
    }

    public RefugeeFormDto getRefugeeById(Long refugeeId) {
        Optional<Refugee> refugee = refugeeRepository.findById(refugeeId);
        if (refugee.isPresent()) {
            return refugeeMapper.mapRefugeeFormDto(refugee.get());
        }
        return null;
    }

    public Refugee saveRefugee(RefugeeFormDto refugeeDto) {
        Refugee refugee = refugeeMapper.mapNewRefugee(refugeeDto);
        return refugeeRepository.save(refugee);
    }

    @Transactional
    public int updateRefugee(RefugeeFormDto refugeeDto) {
        return refugeeRepository.updateRefugee(refugeeDto.getId(), refugeeDto.getIdentificationNumber(), refugeeDto.getFirstName(), refugeeDto.getLastName());
    }

    public Map<Long, String> findByIdentificationNumber(String identificationNumber) {
        List<Refugee> productList = refugeeRepository.findByIdentificationNumber(identificationNumber);
        return productList.stream()
                .collect(Collectors.toMap(Refugee::getId, p -> p.getIdentificationNumber()));
    }

    @Transactional
    public int deactivateRefugee(Long refugeeId) {
        return refugeeRepository.updateRefugeeStatus(refugeeId, 0);
    }

    @Transactional
    public int activateRefugee(Long refugeeId) {
        return refugeeRepository.updateRefugeeStatus(refugeeId, 1);
    }
}
