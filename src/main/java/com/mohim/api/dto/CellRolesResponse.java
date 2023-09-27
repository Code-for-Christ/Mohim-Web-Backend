package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CellRolesResponse {
    List<CellRoleDTO> cellRoles;

    public static CellRolesResponse from(List<CellRoleDTO> cellRoleDTOS) {
        return CellRolesResponse.builder()
                .cellRoles(cellRoleDTOS)
                .build();
    }
}
