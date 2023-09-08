package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MinistryRoleResponse {
    private Integer ministryId;
    private String ministryName;
    private String ministryRole;

}
