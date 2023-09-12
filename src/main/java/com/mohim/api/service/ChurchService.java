package com.mohim.api.service;

import com.mohim.api.domain.Church;
import com.mohim.api.dto.ChurchDTO;
import com.mohim.api.dto.ChurchesResponse;
import com.mohim.api.mapper.ChurchMapper;
import com.mohim.api.repository.ChurchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChurchService {

    private final ChurchRepository churchRepository;
    private final ChurchMapper churchMapper;

    public ChurchesResponse getChurchList() {

        List<Church> churches = churchRepository.findAll();

        List<ChurchDTO> churchDTOS = churches.stream()
                .map(church -> {
                    return churchMapper.toChurchDTO(church);
                })
                .collect(Collectors.toList());

        return churchMapper.toChurchesResponse(churchDTOS);
    }
}
