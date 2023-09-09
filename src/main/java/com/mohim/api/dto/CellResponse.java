package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CellResponse {
    private Long id;
    private Integer parish;
    private Integer cell;
    private Long churchId;
}
