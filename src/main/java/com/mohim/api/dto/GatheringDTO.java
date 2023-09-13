package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GatheringDTO {
    private Long id;
    private String name;
    private Long churchId;
}
