package com.mohim.api.service;

import com.mohim.api.domain.Cell;
import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.*;
import com.mohim.api.mapper.CellMapper;
import com.mohim.api.repository.CellRepository;
import com.mohim.api.repository.CellRoleRepository;
import com.mohim.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CellService {

    private final CellRepository cellRepository;
    private final CellMapper cellMapper;
    private final MemberRepository memberRepository;
    private final CellRoleRepository cellRoleRepository;

    public CellsResponse getCellList(Long churchId) {
        List<Cell> cellList = cellRepository.findAllByChurchId(churchId);

        List<CellDTO> cellDTOS = cellList.stream()
                .map(cellMapper::toCellDTO)
                .collect(Collectors.toList());

        return cellMapper.toCellsResponse(cellDTOS);
    }

    public CellLeadersResponse getCellLeaders(Long churchId, Long cellId) {
        // 해당 셀 목록 가져오기
        // 셀에 역할
        List<CellLeaderDTO> cellLeaderDTOS = memberRepository.findChurchMembersByCellIdAndChurchId(cellId, churchId).stream()
                .map(churchMember ->
                    cellMapper.toCellLeaderDTO(churchMember)
                ).collect(Collectors.toList());

        return cellMapper.toCellLeadersResponse(cellLeaderDTOS);
    }

    public CellRolesResponse getCellRoles(Long churchId) {
        List<CellRoleDTO> cellRoleDTOS = cellRoleRepository.findByChurchId(churchId).stream()
                .map(cellRole -> CellRoleDTO.from(cellRole))
                .collect(Collectors.toList());
        return CellRolesResponse.from(cellRoleDTOS);
    }
}
