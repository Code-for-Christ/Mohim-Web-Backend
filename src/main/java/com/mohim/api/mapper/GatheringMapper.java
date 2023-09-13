package com.mohim.api.mapper;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.Gathering;
import com.mohim.api.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatheringMapper {

    public GatheringDTO toGatheringDTO(Gathering gathering) {
        return GatheringDTO.builder()
                .id(gathering.getId())
                .name(gathering.getName())
                .churchId(gathering.getChurch().getId())
                .build();
    }

    public GatheringsResponse toGatheringsResponse(List<GatheringDTO> gatheringDTOS) {
        return GatheringsResponse.builder()
                .gatherings(gatheringDTOS)
                .build();
    }

    public GatheringLeaderDTO toGatheringLeaderDTO(ChurchMember churchMember) {
        return GatheringLeaderDTO.builder()
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
                .gatheringRole(churchMember.getChurchMemberGatheringRoleAssociations().get(0).getGatheringRole().getRole())
                .build();
    }

    public GatheringLeadersResponse toGatheringLeadersResponse(List<GatheringLeaderDTO> gatheringLeaderDTOS) {
        return GatheringLeadersResponse.builder()
                .churchMembers(gatheringLeaderDTOS)
                .build();
    }
}
