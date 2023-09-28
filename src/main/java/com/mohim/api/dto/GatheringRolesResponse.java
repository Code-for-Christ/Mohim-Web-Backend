package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GatheringRolesResponse {
    private List<GatheringRoleDTO> gatheringRoles;

    public static GatheringRolesResponse from(List<GatheringRoleDTO> gatheringRoleDTOS) {
        return GatheringRolesResponse.builder()
                .gatheringRoles(gatheringRoleDTOS)
                .build();
    }
}
