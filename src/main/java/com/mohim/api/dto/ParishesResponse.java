package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParishesResponse {
    private List<Integer> parishes;

    public static ParishesResponse from(List<Integer> parishes){
        return ParishesResponse.builder()
                .parishes(parishes)
                .build();
    }
}
