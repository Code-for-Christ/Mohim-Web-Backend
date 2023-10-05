package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteChurchMemberResponse {
    Long id;

    public static DeleteChurchMemberResponse from(Long id) {
        return DeleteChurchMemberResponse.builder()
                .id(id)
                .build();
    }
}
