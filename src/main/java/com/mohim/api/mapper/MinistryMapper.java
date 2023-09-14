package com.mohim.api.mapper;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.Ministry;
import com.mohim.api.dto.MinistriesResponse;
import com.mohim.api.dto.MinistryDTO;
import com.mohim.api.dto.MinistryLeaderDTO;
import com.mohim.api.dto.MinistryLeadersResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MinistryMapper {

    public MinistryDTO toMinistryDTO(Ministry ministry){
        return MinistryDTO.builder()
                .id(ministry.getId())
                .name(ministry.getName())
                .category(ministry.getCategory())
                .churchId(ministry.getChurch().getId())
                .build();
    }

    public MinistriesResponse toMinistryResponse(List<MinistryDTO> ministryDTOS) {
        return MinistriesResponse.builder()
                .ministries(ministryDTOS)
                .build();
    }

    public MinistryLeaderDTO toMinistryLeaderDTO(ChurchMember churchMember) {
        return MinistryLeaderDTO.builder()
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
                .ministryId(churchMember.getChurchMemberMinistryRoleAssociations().get(0).getId())
                .ministryName(churchMember.getChurchMemberMinistryRoleAssociations().get(0).getMinistryRole().getMinistry().getName())
                .ministryRole(churchMember.getChurchMemberMinistryRoleAssociations().get(0).getMinistryRole().getRole())
                .build();
    }

    public MinistryLeadersResponse toMinistryLeadersResponse(List<MinistryLeaderDTO> ministryLeaderDTOS) {
        return MinistryLeadersResponse.builder()
                .churchMembers(ministryLeaderDTOS)
                .build();
    }
}
