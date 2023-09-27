package com.mohim.api.dto;

import com.mohim.api.domain.CellRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CellRoleDTO {
    private Long id;
    private Long cellId;
    private String role;
    private Integer ordinalPosition;

    public static CellRoleDTO from (CellRole cellRole) {
        return CellRoleDTO.builder()
                .id(cellRole.getId())
                .cellId(cellRole.getCell().getId())
                .role(cellRole.getRole())
                .ordinalPosition(cellRole.getOrdinalPosition())
                .build();
    }
}
