package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PositionsResponse {
    private List<PositionDTO> positions;

    public static PositionsResponse from(List<PositionDTO> positionDTOS) {
        return PositionsResponse.builder()
                .positions(positionDTOS)
                .build();
    }
}
