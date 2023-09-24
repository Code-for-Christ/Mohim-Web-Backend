package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParishLeadersResponse {
    private List<ParishLeaderDTO> churchMembers;

    public static ParishLeadersResponse from(List<ParishLeaderDTO> parishLeaderDTOS){
        return ParishLeadersResponse.builder()
                .churchMembers(parishLeaderDTOS)
                .build();
    }
}
