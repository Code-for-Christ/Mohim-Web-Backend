package com.mohim.api.mapper;

import com.mohim.api.domain.Cell;
import com.mohim.api.dto.CellDTO;
import com.mohim.api.dto.CellsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CellMapper {

    public CellDTO toCellDTO(Cell cell){
        return CellDTO.builder()
                .id(cell.getId())
                .cell(cell.getCell())
                .parish(cell.getParish().getId().intValue())
                .churchId(cell.getChurch().getId())
                .build();
    }

    public CellsResponse toCellResponse(List<CellDTO> cellDTOs) {
        return CellsResponse.builder()
                .cells(cellDTOs)
                .build();
    }
}
