package com.mohim.api.service;

import com.mohim.api.dto.ParishesResponse;
import com.mohim.api.repository.ParishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParishService {

    private final ParishRepository parishRepository;

    public ParishesResponse getParishList(Long churchId) {
        List<Integer> parishes = parishRepository.findByChurchId(churchId).stream()
                .map(parish -> parish.getId().intValue())
                .collect(Collectors.toList());
        return ParishesResponse.from(parishes);
    }
}
