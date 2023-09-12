package com.mohim.api.mapper;

import com.mohim.api.domain.Cell;
import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.CellDTO;
import com.mohim.api.dto.CellLeaderDTO;
import com.mohim.api.dto.CellLeadersResponse;
import com.mohim.api.dto.CellsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CellMapper {

    public CellLeaderDTO toCellLeaderDTO(ChurchMember churchMember) {
        return CellLeaderDTO.builder()
                .id(churchMember.getId())
                .name(churchMember.getName())
                .householderId(churchMember.getHouseholderId())
                .relationshipWithHouseholder(churchMember.getRelationshipWithHouseHolder())
                .birthYear(churchMember.getBirthYear())
                .salvationYear(churchMember.getSalvationYear())
                .salvationMonth(churchMember.getSalvationMonth())
                .salvationDay(churchMember.getSalvationDay())
                .carNumber(churchMember.getCarNumber())
                .sex(churchMember.getGender().getValue())
                .phoneNumber(churchMember.getPhoneNumber())
                .address(churchMember.getAddress())
                .profileImageThumbnail(churchMember.getProfileImageThumbnail())
                .churchId(churchMember.getChurch().getId())
                .churchName(churchMember.getChurch().getName())
                .cellId(churchMember.getCell().getId())
                .cell(churchMember.getCell().getCell())
                .gatheringId(churchMember.getGathering() != null? churchMember.getGathering().getId() : null)
                .gatheringName(churchMember.getGathering() != null? churchMember.getGathering().getName() : null)
                .positionId(churchMember.getPosition() != null ? churchMember.getPosition().getId() : null)
                .positionName(churchMember.getPosition() != null ? churchMember.getPosition().getName() : null)
                .cellRole(churchMember.getChurchMemberCellRoleAssociations().get(0).getCellRole().getRole())
                .build();
    }

    public CellLeadersResponse toCellLeadersResponse(List<CellLeaderDTO> cellLeaderDTOS) {
        return CellLeadersResponse.builder()
                .churchMembers(cellLeaderDTOS)
                .build();
    }

    public CellDTO toCellDTO(Cell cell){
        return CellDTO.builder()
                .id(cell.getId())
                .cell(cell.getCell())
                .parish(cell.getParish().getId().intValue())
                .churchId(cell.getChurch().getId())
                .build();
    }

    public CellsResponse toCellsResponse(List<CellDTO> cellDTOs) {
        return CellsResponse.builder()
                .cells(cellDTOs)
                .build();
    }
}
