package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MinistryRolesResponse {
    List<MinistryRoleDTO> ministryRoles;

    public static MinistryRolesResponse from(List<MinistryRoleDTO> ministryRoleDTOS) {
        return MinistryRolesResponse.builder()
                .ministryRoles(ministryRoleDTOS)
                .build();
    }
}
