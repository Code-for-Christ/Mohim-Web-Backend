package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateChurchMemberResponse {
    Long id;

    public static UpdateChurchMemberResponse from(Long id) {
        return UpdateChurchMemberResponse.builder()
                .id(id)
                .build();
    }
}
