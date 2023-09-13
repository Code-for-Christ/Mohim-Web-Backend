package com.mohim.api.mapper;

import com.mohim.api.domain.Gathering;
import com.mohim.api.dto.GatheringDTO;
import com.mohim.api.dto.GatheringsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatheringMapper {

    public GatheringDTO toGatheringDTO(Gathering gathering) {
        return GatheringDTO.builder()
                .id(gathering.getId())
                .name(gathering.getName())
                .churchId(gathering.getChurch().getId())
                .build();
    }

    public GatheringsResponse toGatheringsResponse(List<GatheringDTO> gatheringDTOS) {
        return GatheringsResponse.builder()
                .gatherings(gatheringDTOS)
                .build();
    }
}
