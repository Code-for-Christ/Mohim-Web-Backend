package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CellDTO {
    private Long id;
    private Integer parish;
    private Integer cell;
    private Long churchId;
}
