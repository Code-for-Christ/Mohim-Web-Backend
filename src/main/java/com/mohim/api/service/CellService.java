package com.mohim.api.service;

import com.mohim.api.domain.Cell;
import com.mohim.api.dto.CellDTO;
import com.mohim.api.dto.CellsResponse;
import com.mohim.api.mapper.CellMapper;
import com.mohim.api.repository.CellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CellService {

    private final CellRepository cellRepository;
    private final CellMapper cellMapper;

    public CellsResponse getCellList(Long churchId) {
        List<Cell> cellList = cellRepository.findAllByChurchId(churchId);

        List<CellDTO> cellDTOS = cellList.stream()
                .map(cellMapper::toCellDTO)
                .collect(Collectors.toList());

        return cellMapper.toCellsResponse(cellDTOS);
    }
}
