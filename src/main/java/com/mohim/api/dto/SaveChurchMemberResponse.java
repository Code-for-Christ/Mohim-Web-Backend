package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveChurchMemberResponse {
    private Long id;

    public static SaveChurchMemberResponse from(Long id) {
        return SaveChurchMemberResponse.builder()
                .id(id)
                .build();
    }
}
