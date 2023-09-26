package com.mohim.api.dto;

import com.mohim.api.domain.ChurchMember;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ParishLeaderDTO {
    private Long id;
    private String name;
    private Long householderId;
    private String relationshipWithHouseholder;
    private Integer birthYear;
    private Integer salvationYear;
    private Integer salvationMonth;
    private Integer salvationDay;
    private String carNumber;
    private String sex;
    private String phoneNumber;
    private String address;
    private String profileImageThumbnail;
    private Long churchId;
    private String churchName;
    private Long cellId;
    private Integer cell;
    private Long gatheringId;
    private String gatheringName;
    private Long positionId;
    private String positionName;
    private Integer parish;
    private String parishRole;

    public static ParishLeaderDTO from(ChurchMember churchMember){
        return ParishLeaderDTO.builder()
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
                .parish(churchMember.getChurchMemberParishRoleAssociations().get(0).getParishRole().getParish().getId().intValue())
                .parishRole(churchMember.getChurchMemberCellRoleAssociations().get(0).getCellRole().getRole())
                .build();
    }
}
