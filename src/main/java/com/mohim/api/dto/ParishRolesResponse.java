package com.mohim.api.dto;

import com.mohim.api.domain.ParishRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ParishRolesResponse {
    List<ParishRoleDTO> parishRoles;

    public static ParishRolesResponse from (List<ParishRoleDTO> parishRoleDTOS) {
        return ParishRolesResponse.builder()
                .parishRoles(parishRoleDTOS)
                .build();
    }
}
