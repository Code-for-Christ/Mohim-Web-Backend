package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChurchDTO {
    private Long id;
    private String name;
    private String country;
}
