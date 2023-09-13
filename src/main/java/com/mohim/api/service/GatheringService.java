package com.mohim.api.service;

import com.mohim.api.domain.Gathering;
import com.mohim.api.dto.GatheringDTO;
import com.mohim.api.dto.GatheringsResponse;
import com.mohim.api.mapper.GatheringMapper;
import com.mohim.api.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final GatheringMapper gatheringMapper;

    public GatheringsResponse getGatheringList(Long churchId) {
        List<GatheringDTO> gatheringDTOS = gatheringRepository.findAll().stream()
                .map(gathering -> {
                    return gatheringMapper.toGatheringDTO(gathering);
                })
                .collect(Collectors.toList());

        return gatheringMapper.toGatheringsResponse(gatheringDTOS);
    }
}
