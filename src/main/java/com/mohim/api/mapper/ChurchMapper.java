package com.mohim.api.mapper;

import com.mohim.api.domain.Church;
import com.mohim.api.dto.ChurchDTO;
import com.mohim.api.dto.ChurchesResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChurchMapper {
    public ChurchesResponse toChurchesResponse(List<ChurchDTO> churchDTOS) {
        return ChurchesResponse.builder()
                .churches(churchDTOS)
                .build();
    }

    public ChurchDTO toChurchDTO(Church church){
        return ChurchDTO.builder()
                .id(church.getId())
                .name(church.getName())
                .country(church.getCountry())
                .build();
    }
}
