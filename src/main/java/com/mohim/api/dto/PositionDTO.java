package com.mohim.api.dto;

import com.mohim.api.domain.Position;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PositionDTO {
    private Long id;
    private String name;
    private Long churchId;

    public static PositionDTO from(Position position) {
        return PositionDTO.builder()
                .id(position.getId())
                .name(position.getName())
                .churchId(position.getChurch().getId())
                .build();
    }
}
