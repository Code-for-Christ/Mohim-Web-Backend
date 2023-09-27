package com.mohim.api.service;

import com.mohim.api.domain.*;
import com.mohim.api.dto.ChurchMemberResponse;
import com.mohim.api.dto.ChurchMembersRequest;
import com.mohim.api.dto.ChurchMembersResponse;
import com.mohim.api.dto.MinistryRoleResponse;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mohim.api.domain.QChurch.church;
import static com.mohim.api.domain.QChurchMember.churchMember;
import static com.mohim.api.domain.QParish.parish;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ChurchMemberMinistryAssociationRepository churchMemberMinistryAssociationRepository;
    private final ChurchMemberMinistryRoleAssociationRepository churchMemberMinistryRoleAssociationRepository;
    private final ChurchMemberCellRoleAssociationRepository churchMemberCellRoleAssociationRepository;
    private final ChurchMemberGatheringRoleAssociationRepository churchMemberGatheringRoleAssociationRepository;
    private final ChurchMemberParishRoleAssociationRepository churchMemberParishRoleAssociationRepository;

    public ChurchMembersResponse getChurchMembers(Integer churchId, ChurchMembersRequest request, HttpServletRequest httpServletRequest) {
        List<ChurchMembersResponse.ChurchMember> churchMembers = memberRepository.findByChurchId(churchId, request).stream()
                .map(churchMember -> {
                    ChurchMember householderMember = memberRepository.findById(churchMember.getHouseholderId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
                    return ChurchMembersResponse.ChurchMember.builder()
                            .id(churchMember.getId())
                            .name(churchMember.getName())
                            .householderId(churchMember.getHouseholderId())
                            .householderName(householderMember.getName())
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
                            .cell(churchMember.getCell().getCell())
                            .cellId(churchMember.getCell().getId())
                            .gatheringId(churchMember.getGathering().getId())
                            .gatheringName(churchMember.getGathering().getName())
                            .positionId(churchMember.getPosition() == null? null : churchMember.getPosition().getId())
                            .positionName(churchMember.getPosition() == null? null : churchMember.getPosition().getName())
                            .build();})
                .collect(Collectors.toList());

        Integer totalCount = memberRepository.getTotalCount(churchId, request);

        Integer page = request.getPage();
        Integer size = request.getSize();
        String nextUrl = null;
        if (page * size < totalCount) {
            nextUrl = httpServletRequest.getRequestURL().toString();
            nextUrl += "?" + httpServletRequest.getQueryString();

            String prePage = String.format("page=%d", page);
            String nextPage = String.format("page=%d", page+1);
            nextUrl = nextUrl.replace(prePage, nextPage);
        }


        ChurchMembersResponse.Metadata metadata = ChurchMembersResponse.Metadata.builder()
                .totalCount(totalCount)
                .page(request.getPage())
                .size(request.getSize())
                .orderBy(request.getOrderBy())
                .nextUrl(nextUrl)
                .build();

        return ChurchMembersResponse.builder()
                .metadata(metadata)
                .churchMembers(churchMembers)
                .build();
    }

    public ChurchMemberResponse getChurchMember(Integer churchId, Integer memberId) {
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(memberId.longValue(), churchId.longValue()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Ministry> ministries = churchMemberMinistryAssociationRepository.findAllByChurchMember(churchMember).stream()
                .map(ChurchMemberMinistryAssociation::getMinistry)
                .collect(Collectors.toList());

        List<MinistryRole> ministryRoles = churchMemberMinistryRoleAssociationRepository.findAllByChurchMember(churchMember).stream()
                .map(ChurchMemberMinistryRoleAssociation::getMinistryRole)
                .collect(Collectors.toList());

        List<ChurchMemberResponse.Ministry> ministryResponses = new ArrayList<>();
        for (Ministry ministry : ministries) {
            ChurchMemberResponse.Ministry response = ChurchMemberResponse.Ministry.builder()
                    .id(ministry.getId())
                    .name(ministry.getName())
                    .build();
            ministryResponses.add(response);
        }
        for (MinistryRole ministryRole : ministryRoles){
            for (ChurchMemberResponse.Ministry ministryResponse : ministryResponses) {
                if (Objects.equals(ministryRole.getMinistry().getId(), ministryResponse.getId())) {
                    ministryResponse.setRole(ministryRole.getRole());
                }
            }
        }

        List<ChurchMember> householdMembers = memberRepository.findAllByHouseholderId(churchMember.getHouseholderId());

        List<ChurchMemberResponse.Household> householdResponses = new ArrayList<>();
        for (ChurchMember householdMember : householdMembers) {
            if (householdMember.getId() != memberId.longValue()) {
                ChurchMemberResponse.Household householdResponse = ChurchMemberResponse.Household.builder()
                        .id(householdMember.getId())
                        .name(householdMember.getName())
                        .householderId(householdMember.getHouseholderId())
                        .relationshipWithHouseholder(householdMember.getRelationshipWithHouseHolder())
                        .phoneNumber(householdMember.getPhoneNumber())
                        .profileImageThumbnail(householdMember.getProfileImageThumbnail())
                        .build();
                householdResponses.add(householdResponse);
            }
        }

        ChurchMemberCellRoleAssociation churchMemberCellRoleAssociation = churchMemberCellRoleAssociationRepository.findByChurchMember(churchMember);
        ChurchMemberParishRoleAssociation churchMemberParishRoleAssociation = churchMemberParishRoleAssociationRepository.findByChurchMember(churchMember);
        ChurchMemberGatheringRoleAssociation churchMemberGatheringRoleAssociation = churchMemberGatheringRoleAssociationRepository.findByChurchMember(churchMember);

        return ChurchMemberResponse.builder()
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
                .parish(churchMember.getCell().getParish().getId().intValue())
                .parishRole(churchMemberParishRoleAssociation != null ? churchMemberParishRoleAssociation.getParishRole().getRole() : null)
                .cellId(churchMember.getCell().getId())
                .cell(churchMember.getCell().getCell())
                .cellRole(churchMemberCellRoleAssociation != null ? churchMemberCellRoleAssociation.getCellRole().getRole() : null)
                .gatheringId(churchMember.getGathering().getId())
                .gatheringName(churchMember.getGathering().getName())
                .gatheringRole(churchMemberGatheringRoleAssociation != null ? churchMemberGatheringRoleAssociation.getGatheringRole().getRole() : null)
                .positionId(churchMember.getPosition() != null ? churchMember.getPosition().getId() : null)
                .positionName(churchMember.getPosition() != null ? churchMember.getPosition().getName() : null)
                .household(householdResponses)
                .ministries(ministryResponses)
                .build();
    }
}
