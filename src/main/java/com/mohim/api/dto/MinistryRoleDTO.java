package com.mohim.api.dto;

import com.mohim.api.domain.CellRole;
import com.mohim.api.domain.MinistryRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MinistryRoleDTO {
    private Long id;
    private Long ministryId;
    private String role;
    private Integer ordinalPosition;

    public static MinistryRoleDTO from (MinistryRole ministryRole) {
        return MinistryRoleDTO.builder()
                .id(ministryRole.getId())
                .ministryId(ministryRole.getMinistry().getId())
                .role(ministryRole.getRole())
                .ordinalPosition(ministryRole.getOrdinalPosition())
                .build();
    }
}
