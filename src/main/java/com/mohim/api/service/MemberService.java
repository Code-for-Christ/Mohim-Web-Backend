package com.mohim.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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

    public ChurchMemberResponse getChurchMember(Long churchId, Long memberId) {
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(memberId, churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

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

        List<ChurchMember> householdMembers = memberRepository.findAllByChurchIdAndHouseholderId(churchId, churchMember.getHouseholderId());

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
    public UpdateChurchMemberResponse updateChurchMember(Long churchId, Long memberId, UpdateChurchMemberRequest request) throws IOException {

        // 멤버 검증 및 가져오기
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(memberId, churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 교구 검증 및 가져오기
        Parish upatedParish = parishRepository.findByChurchIdAndId(churchId, request.getParish()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH));
        // 구역 검증 및 가져오기
        Cell updatedCell = cellRepository.findByChurchIdAndId(churchId, request.getCellId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL));

        // TODO finished
        // 교구에 맞는 구역 검증
        if (!updatedCell.getParish().getId().equals(upatedParish.getId())) {
            throw new CustomException(ErrorCode.INVALID_PARISH_OR_CELL);
        }

        // 회(모임) 검증 및 가져오기
        Gathering updatedGathering = gatheringRepository.findByChurchIdAndId(churchId, request.getGatheringId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING));
        // 직분 가져오기
        Position updatedPosition = positionRepository.findByChurchIdAndId(churchId, request.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));

        // 교구 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        // 요청한 parishRole 이 있는지 검증
        ParishRole parishRole = parishRoleRepository.findByChurchIdAndId(churchId, request.getParishRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH_ROLE));
        // 요청한 멤버의 기존 parishRole 이 있는지 조회
        ChurchMemberParishRoleAssociation churchMemberParishRoleAssociation = churchMemberParishRoleAssociationRepository.findByChurchMember(churchMember);

        if (churchMemberParishRoleAssociation == null) { // 없다면 새로 생성
            ChurchMemberParishRoleAssociation createdAssociation = ChurchMemberParishRoleAssociation.createChurchMemberParishRoleAssociation(churchMember, parishRole);
            churchMemberParishRoleAssociationRepository.save(createdAssociation);
        } else { // 있다면 수정
            churchMemberParishRoleAssociation.updateParishRole(parishRole);
            churchMemberParishRoleAssociationRepository.save(churchMemberParishRoleAssociation);
        }

        // 구역 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        // 요청한 cellRole 존재 검증
        CellRole cellRole = cellRoleRepository.findByChurchIdAndId(churchId, request.getCellRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL_ROLE));
        // 요청한 멤버의 기존 cellRole 여부 조회
        ChurchMemberCellRoleAssociation churchMemberCellRoleAssociation = churchMemberCellRoleAssociationRepository.findByChurchMember(churchMember);
        if (churchMemberCellRoleAssociation == null) {
            ChurchMemberCellRoleAssociation createdAssociation = ChurchMemberCellRoleAssociation.createChurchMemberCellRoleAssociation(churchMember, cellRole);
            churchMemberCellRoleAssociationRepository.save(createdAssociation);
        } else {
            churchMemberCellRoleAssociation.updateCellRole(cellRole);
            churchMemberCellRoleAssociationRepository.save(churchMemberCellRoleAssociation);
        }

        // 회별 역할 검증 및 추가
        // TODO churchId 추가
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        // gatheringRole 검증
        GatheringRole gatheringRole = gatheringRoleRepository.findByChurchIdAndId(churchId, request.getGatheringRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING_ROLE));
        // 요청 멤버의 기존 회별 역할 여부 조회
        ChurchMemberGatheringRoleAssociation churchMemberGatheringRoleAssociation = churchMemberGatheringRoleAssociationRepository.findByChurchMember(churchMember);
        if (churchMemberGatheringRoleAssociation == null) {
            ChurchMemberGatheringRoleAssociation createdAssociation = ChurchMemberGatheringRoleAssociation.createChurchMemberGatheringRoleAssociation(churchMember, gatheringRole);
            churchMemberGatheringRoleAssociationRepository.save(createdAssociation);
        } else {
            churchMemberGatheringRoleAssociation.updateGatheringRole(gatheringRole);
            churchMemberGatheringRoleAssociationRepository.save(churchMemberGatheringRoleAssociation);
        }

        // 봉사 및 봉사 역할 검증 및 추가
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        List<UpdateChurchMemberRequest.MinistryDTO> ministries = objectMapper.readValue(request.getMinistries(), new TypeReference<List<UpdateChurchMemberRequest.MinistryDTO>>() {
        });

        List<ChurchMemberMinistryAssociation> associations = new ArrayList<>();
        List<ChurchMemberMinistryRoleAssociation> roleAssociations = new ArrayList<>();

        for (UpdateChurchMemberRequest.MinistryDTO ministryDTO: ministries) {
            // 봉사
            // TODO 먼저 내 ID로 가지고 있는 association 목록을 모두 지우고 받은 정보들을 한번에 save
            Ministry ministry = ministryRepository.findByChurchIdAndId(churchId, ministryDTO.ministryId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MINISTRY));
            ChurchMemberMinistryAssociation churchMemberMinistryAssociation = ChurchMemberMinistryAssociation.createChurchMemberMinistryAssociation(churchMember, ministry);
            associations.add(churchMemberMinistryAssociation);

            // 봉사 역할
            if (ministryDTO.ministryRoleId != null) {
                MinistryRole ministryRole = ministryRoleRepository.findByChurchIdAndId(churchId, ministryDTO.ministryRoleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MINISTRY_ROLE));
                ChurchMemberMinistryRoleAssociation churchMemberMinistryRoleAssociation = ChurchMemberMinistryRoleAssociation.createChurchMemberMinistryRoleAssociation(churchMember, ministryRole);
                roleAssociations.add(churchMemberMinistryRoleAssociation);
            }
        }
        // 문제 없이 추가 완료시 기존 것 삭제
        churchMemberMinistryAssociationRepository.deleteAllByChurchMemberId(memberId);
        churchMemberMinistryRoleAssociationRepository.deleteAllByChurchMemberId(memberId);

        // 벌크 save
        churchMemberMinistryAssociationRepository.saveAll(associations);
        churchMemberMinistryRoleAssociationRepository.saveAll(roleAssociations);

        // 세대주 주소 변경 여부 확인 및 변경
        // TODO householdId가 있다면 새로 업데이트, 없다면 자기로 getUpdateHouseholdAddress 여부에 따라 세대원 주소 바꾸는 것도 고려
        // TODO
        Long updatedHouseholderId = churchMember.getHouseholderId();
        if (request.getHouseholderId() != null) {
            updatedHouseholderId = request.getHouseholderId();
        }

        List<ChurchMember> churchMembers = new ArrayList<>();
        if (request.getUpdateHouseholdAddress()) {
            List<ChurchMember> householdMembers = memberRepository.findAllByChurchIdAndHouseholderId(churchId, updatedHouseholderId);
            for (ChurchMember householdMember : householdMembers) {
                if (!householdMember.getId().equals(memberId)) {
                    householdMember.updateAddress(request.getAddress());
                    churchMembers.add(householdMember);
                }
            }
        }

        // TODO 성도 추가 시에는 플러쉬 해서 없는 ID 미리 받아오는 것 고려 / flush 안하고도 householdId와 자기 id를 맞춰줄수 있는 방법 있는지 확인
        // 정보 업데이트
        churchMember.updateChurchMember(
                request.getName(),
                updatedCell,
                updatedGathering,
                updatedPosition,
                updatedHouseholderId,
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
        churchMembers.add(churchMember);

        // 파일 타입 검증
        if (!request.getProfileImage().isEmpty()) {
            String[] fileNames = request.getProfileImage().getOriginalFilename().split("\\.");
            String fileType = fileNames[fileNames.length - 1];

            boolean isAllowedExtension = false;

            for (String extension : ALLOWED_EXTENSIONS) {
                if (fileType.equalsIgnoreCase(extension)){
                    isAllowedExtension = true;
                    break;
                }
            }
            if (!isAllowedExtension) {
                throw new CustomException(ErrorCode.INVALID_EXTENSION);
            }

            // 이미지 업로드
            profileImageService.uploadProfileImage(churchMember, request.getProfileImage(), fileType);
        }

        // 커밋
        memberRepository.saveAll(churchMembers);

        return UpdateChurchMemberResponse.from(memberId);
    }
}
