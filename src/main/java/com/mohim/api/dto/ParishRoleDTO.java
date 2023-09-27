package com.mohim.api.dto;

import com.mohim.api.domain.ParishRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ParishRoleDTO {
    private Long id;
    private Long parishId;
    private String role;
    private Integer ordinalPosition;

    public static ParishRoleDTO from (ParishRole parishRole) {
        return ParishRoleDTO.builder()
                .id(parishRole.getId())
                .parishId(parishRole.getParish().getId())
                .role(parishRole.getRole())
                .ordinalPosition(parishRole.getOrdinalPosition())
                .build();
    }
}
