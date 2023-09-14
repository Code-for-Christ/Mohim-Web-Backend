package com.mohim.api.mapper;

import com.mohim.api.domain.Ministry;
import com.mohim.api.dto.MinistriesResponse;
import com.mohim.api.dto.MinistryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MinistryMapper {

    public MinistryDTO toMinistryDTO(Ministry ministry){
        return MinistryDTO.builder()
                .id(ministry.getId())
                .name(ministry.getName())
                .category(ministry.getCategory())
                .churchId(ministry.getChurch().getId())
                .build();
    }

    public MinistriesResponse toMinistryResponse(List<MinistryDTO> ministryDTOS) {
        return MinistriesResponse.builder()
                .ministries(ministryDTOS)
                .build();
    }
}
