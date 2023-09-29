package com.mohim.api.service;

import com.mohim.api.constant.Gender;
import com.mohim.api.domain.*;
import com.mohim.api.dto.*;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final ParishRepository parishRepository;
    private final CellRepository cellRepository;
    private final GatheringRepository gatheringRepository;
    private final PositionRepository positionRepository;
    private final MinistryRepository ministryRepository;

    private final ParishRoleRepository parishRoleRepository;
    private final CellRoleRepository cellRoleRepository;
    private final GatheringRoleRepository gatheringRoleRepository;
    private final MinistryRoleRepository ministryRoleRepository;

    private final ProfileImageService profileImageService;

    private final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};

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
                    ministryResponse.setRoleId(ministryRole.getId());
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
                .parishRoleId(churchMemberParishRoleAssociation != null ? churchMemberParishRoleAssociation.getParishRole().getId() : null)
                .parishRole(churchMemberParishRoleAssociation != null ? churchMemberParishRoleAssociation.getParishRole().getRole() : null)
                .cellId(churchMember.getCell().getId())
                .cell(churchMember.getCell().getCell())
                .cellRoleId(churchMemberCellRoleAssociation != null ? churchMemberCellRoleAssociation.getCellRole().getId() : null)
                .cellRole(churchMemberCellRoleAssociation != null ? churchMemberCellRoleAssociation.getCellRole().getRole() : null)
                .gatheringId(churchMember.getGathering().getId())
                .gatheringName(churchMember.getGathering().getName())
                .gatheringRoleId(churchMemberGatheringRoleAssociation != null ? churchMemberGatheringRoleAssociation.getGatheringRole().getId() : null)
                .gatheringRole(churchMemberGatheringRoleAssociation != null ? churchMemberGatheringRoleAssociation.getGatheringRole().getRole() : null)
                .positionId(churchMember.getPosition() != null ? churchMember.getPosition().getId() : null)
                .positionName(churchMember.getPosition() != null ? churchMember.getPosition().getName() : null)
                .household(householdResponses)
                .ministries(ministryResponses)
                .build();
    }

    @Transactional
    public void updateChurchMember(Long churchId, Long memberId, UpdateChurchMemberRequest request) throws IOException {

        // 멤버 검증 및 가져오기
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(memberId, churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        ChurchMember houseHoldMember = memberRepository.findByIdAndChurchId(churchMember.getHouseholderId(), churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        String[] fileNames = request.getProfileImage().getOriginalFilename().split("\\.");
        String fileName = fileNames[fileNames.length - 1];

        for (String extension : ALLOWED_EXTENSIONS) {
            if (extension.equalsIgnoreCase(fileName)){
                throw new CustomException(ErrorCode.INVALID_EXTENSION);
            }
        }

        profileImageService.uploadProfileImage(churchMember, request.getProfileImage(), fileName);

        // 교구에 맞는 구역 검증
        if (request.getCellId() / request.getParish() != 10) {
            throw new CustomException(ErrorCode.INVALID_PARISH_OR_CELL);
        }

        // 교구 검증 및 가져오기
        Parish upatedParish = parishRepository.findById(request.getParish()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH));
        // 구역 검증 및 가져오기
        Cell updatedCell = cellRepository.findById(request.getCellId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL));
        // 회(모임) 검증 및 가져오기
        Gathering updatedGathering = gatheringRepository.findById(request.getGatheringId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING));
        // 전도인 역할 가져오기
        Position updatedPosition = positionRepository.findById(request.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));

        // 교구 역할 검증 및 추가
        ParishRole parishRole = parishRoleRepository.findById(request.getParishRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH_ROLE));
        ChurchMemberParishRoleAssociation churchMemberParishRoleAssociation = ChurchMemberParishRoleAssociation.createChurchMemberParishRoleAssociation(churchMember, parishRole);
        churchMemberParishRoleAssociationRepository.save(churchMemberParishRoleAssociation);

        // 구역 역할 검증 및 추가
        CellRole cellRole = cellRoleRepository.findById(request.getCellRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL_ROLE));
        ChurchMemberCellRoleAssociation churchMemberCellRoleAssociation = ChurchMemberCellRoleAssociation.createChurchMemberCellRoleAssociation(churchMember, cellRole);
        churchMemberCellRoleAssociationRepository.save(churchMemberCellRoleAssociation);

        // 회별 역할 검증 및 추가
        GatheringRole gatheringRole = gatheringRoleRepository.findById(request.getGatheringRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING_ROLE));
        ChurchMemberGatheringRoleAssociation churchMemberGatheringRoleAssociation = ChurchMemberGatheringRoleAssociation.createChurchMemberGatheringRoleAssociation(churchMember, gatheringRole);
        churchMemberGatheringRoleAssociationRepository.save(churchMemberGatheringRoleAssociation);

        // 봉사 및 봉사 역할 검증 및 추가
        for (UpdateChurchMemberRequest.MinistryDTO ministryDTO: request.getMinistries()) {
            // 봉사
            Ministry ministry = ministryRepository.findById(ministryDTO.ministryId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MINISTRY));
            ChurchMemberMinistryAssociation churchMemberMinistryAssociation = ChurchMemberMinistryAssociation.createChurchMemberMinistryAssociation(churchMember, ministry);
            churchMemberMinistryAssociationRepository.save(churchMemberMinistryAssociation);

            // 봉사 역할
            if (ministryDTO.ministryRoleId != null) {
                MinistryRole ministryRole = ministryRoleRepository.findById(ministryDTO.ministryRoleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MINISTRY_ROLE));
                ChurchMemberMinistryRoleAssociation churchMemberMinistryRoleAssociation = ChurchMemberMinistryRoleAssociation.createChurchMemberMinistryRoleAssociation(churchMember, ministryRole);
                churchMemberMinistryRoleAssociationRepository.save(churchMemberMinistryRoleAssociation);
            }
        }

        // 세대주 주소 변경 여부 확인 및 변경
        if (request.getUpdateHouseholdAddress()) {
            houseHoldMember.updateAddress(request.getAddress());
            memberRepository.save(houseHoldMember);
        }

        // 정보 업데이트
        churchMember.updateChurchMember(
                request.getName(),
                updatedCell,
                updatedGathering,
                updatedPosition,
                request.getHouseholderId(),
                request.getRelationshipWithHouseholder(),
                request.getBirthYear(),
                request.getSalvationYear(),
                request.getSalvationMonth(),
                request.getSalvationDay(),
                request.getCarNumber(),
                Gender.fromCode(request.getSex()),
                request.getPhoneNumber(),
                request.getAddress()
                );

        memberRepository.save(churchMember);
    }
}
