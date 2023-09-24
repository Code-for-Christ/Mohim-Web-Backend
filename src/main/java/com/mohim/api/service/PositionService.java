package com.mohim.api.service;

import com.mohim.api.dto.PositionDTO;
import com.mohim.api.dto.PositionsResponse;
import com.mohim.api.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    public PositionsResponse getPositionList(Long churchId) {
        List<PositionDTO> positionDTOS = positionRepository.findByChurchId(churchId).stream()
                .map(position -> PositionDTO.from(position))
                .collect(Collectors.toList());
        return PositionsResponse.from(positionDTOS);
    }
}
