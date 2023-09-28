package com.mohim.api.dto;

import com.mohim.api.domain.GatheringRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GatheringRoleDTO {
    private Long id;
    private Long gatheringId;
    private String role;
    private Integer ordinalPosition;

    public static GatheringRoleDTO from(GatheringRole gatheringRole) {
        return GatheringRoleDTO.builder()
                .id(gatheringRole.getId())
                .gatheringId(gatheringRole.getGathering().getId())
                .role(gatheringRole.getRole())
                .ordinalPosition(gatheringRole.getOrdinalPosition())
                .build();
    }
}
