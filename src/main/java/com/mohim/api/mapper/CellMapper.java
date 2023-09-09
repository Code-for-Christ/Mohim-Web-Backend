package com.mohim.api.mapper;

import com.mohim.api.domain.Cell;
import com.mohim.api.dto.CellResponse;
import org.springframework.stereotype.Component;

@Component
public class CellMapper {

    public CellResponse toCellResponse(Cell cell) {
        return CellResponse.builder()
                .id(cell.getId())
                .cell(cell.getCell())
                .parish(cell.getParish().getId().intValue())
                .churchId(cell.getChurch().getId())
                .build();
    }
}
