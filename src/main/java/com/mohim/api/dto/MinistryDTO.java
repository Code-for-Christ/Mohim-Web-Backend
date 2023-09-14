package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MinistryDTO {
    private Long id;
    private String name;
    private String category;
    private Long churchId;
}

