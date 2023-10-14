package com.mohim.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
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

    private final AuthRepository authRepository;
    private final ParishRepository parishRepository;
    private final CellRepository cellRepository;
    private final GatheringRepository gatheringRepository;
    private final PositionRepository positionRepository;
    private final MinistryRepository ministryRepository;
    private final ChurchRepository churchRepository;

    private final ParishRoleRepository parishRoleRepository;
    private final CellRoleRepository cellRoleRepository;
    private final GatheringRoleRepository gatheringRoleRepository;
    private final MinistryRoleRepository ministryRoleRepository;

    private final ProfileImageService profileImageService;

    @PersistenceContext
    private EntityManager entityManager;

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
        Position updatedPosition = churchMember.getPosition();
        if (request.getPositionId() != null) {
            // 직분 가져오기
            updatedPosition = positionRepository.findByChurchIdAndId(churchId, request.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
        } else {
            updatedPosition = null;
        }

        // 교구 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getParishRoleId() != null) {
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
        } else {
            // 요청한 역할id가 없을 경우 기존 역할 정보가 있다면 삭제
            ChurchMemberParishRoleAssociation churchMemberParishRoleAssociation = churchMemberParishRoleAssociationRepository.findByChurchMember(churchMember);
            if (churchMemberParishRoleAssociation != null) {
                churchMemberParishRoleAssociationRepository.delete(churchMemberParishRoleAssociation);
            }
        }


        // 구역 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getCellRoleId() != null) {
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
        } else {
            // 요청한 역할id가 없을 경우 기존 역할 정보가 있다면 삭제
            ChurchMemberCellRoleAssociation churchMemberCellRoleAssociation = churchMemberCellRoleAssociationRepository.findByChurchMember(churchMember);
            if (churchMemberCellRoleAssociation != null) {
                churchMemberCellRoleAssociationRepository.delete(churchMemberCellRoleAssociation);
            }
        }

        // 회별 역할 검증 및 추가
        // TODO churchId 추가
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getGatheringRoleId() != null) {
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
        } else {
            ChurchMemberGatheringRoleAssociation churchMemberGatheringRoleAssociation = churchMemberGatheringRoleAssociationRepository.findByChurchMember(churchMember);
            if (churchMemberGatheringRoleAssociation != null) {
                churchMemberGatheringRoleAssociationRepository.delete(churchMemberGatheringRoleAssociation);
            }
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
        Long updatedHouseholderId = null;

        // HouseholderId 가 존재할 경우 업데이트
        if (request.getHouseholderId() != null) {
            updatedHouseholderId = request.getHouseholderId();
        } else { // 없을 경우, 자기 자신
            updatedHouseholderId = churchMember.getId();
            request.setRelationshipWithHouseholder("본인");
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

        // profileImage != "" && deleteProfileImage: false
        // -> 새로운 프로필사진으로 업데이트
        if (request.getProfileImage() != null && !request.getDeleteProfileImage()) {
            // 파일 타입 검증
            String[] fileNames = request.getProfileImage().getOriginalFilename().split("\\.");
            String fileType = fileNames[fileNames.length - 1];

            boolean isAllowedExtension = false;

            for (String extension : ALLOWED_EXTENSIONS) {
                if (fileType.equalsIgnoreCase(extension)) {
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
        // profileImage =="" && deleteProfileImage: true
        // -> 기존 프로필사진이 존재한다면 삭제 (아예 프로필사진이 존재하지 않는 경우도 포함)
        else if (request.getProfileImage() == null && request.getDeleteProfileImage()) {
            profileImageService.deleteProfileImage(churchMember);
        }
        // 위 두 경우가 아닐 경우에는 profileImage =="" && deleteProfileImage: false -> 기존 프로필사진 그대로 유지

        // 업데이트
        memberRepository.saveAll(churchMembers);

        return UpdateChurchMemberResponse.from(memberId);
    }

    public SaveChurchMemberResponse saveChurchMember(Long churchId, SaveChurchMemberRequest request) throws IOException {
        // 교회 검증 및 가져오기
        Church church = churchRepository.findById(churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHURCH));

        // 교구 검증 및 가져오기
        Parish parish = parishRepository.findByChurchIdAndId(churchId, request.getParish()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH));
        // 구역 검증 및 가져오기
        Cell cell = cellRepository.findByChurchIdAndId(churchId, request.getCellId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL));

        // TODO finished
        // 교구에 맞는 구역 검증
        if (!cell.getParish().getId().equals(parish.getId())) {
            throw new CustomException(ErrorCode.INVALID_PARISH_OR_CELL);
        }

        // 회(모임) 검증 및 가져오기
        Gathering gathering = gatheringRepository.findByChurchIdAndId(churchId, request.getGatheringId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING));

        Position position = null;
        if (request.getPositionId() != null) {
            // 요청한 직분이 있다면 가져오기
            position = positionRepository.findByChurchIdAndId(churchId, request.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
        }

        // 멤버 객체 생성
        ChurchMember churchMember = ChurchMember.create(
                request.getName(),
                church,
                cell,
                gathering,
                position,
                0L,
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

        // TODO flush 하여 chruchMember 선반영 (id 및 관계를 위함)
        entityManager.persist(churchMember);
        entityManager.flush();

        // 세대주 주소 변경 여부 확인 및 변경
        // TODO householdId가 있다면 새로 업데이트, 없다면 자기로 getUpdateHouseholdAddress 여부에 따라 세대원 주소 바꾸는 것도 고려
        // TODO
        // HouseholderId 가 존재할 경우 설정
        if (request.getHouseholderId() != null) {
            churchMember.updateHouseholderId(request.getHouseholderId());
        } else { // 없을 경우, 자기 자신
            // 관계가 본인인지 검증
            if (!request.getRelationshipWithHouseholder().equals("본인")){
                throw new CustomException(ErrorCode.INVALID_RELATIONSHIP);
            }
            Long householderId = churchMember.getId();
            churchMember.updateHouseholderId(householderId);
        }

        Long householderId = churchMember.getHouseholderId();

        List<ChurchMember> churchMembers = new ArrayList<>();
        if (request.getUpdateHouseholdAddress()) {
            List<ChurchMember> householdMembers = memberRepository.findAllByChurchIdAndHouseholderId(churchId, householderId);
            for (ChurchMember householdMember : householdMembers) {
                if (!householdMember.getId().equals(churchMember.getId())) {
                    householdMember.updateAddress(request.getAddress());
                    churchMembers.add(householdMember);
                }
            }
        }

        // 교구 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getParishRoleId() != null) {
            // 요청한 parishRole 이 있는지 검증
            ParishRole parishRole = parishRoleRepository.findByChurchIdAndId(churchId, request.getParishRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARISH_ROLE));
            ChurchMemberParishRoleAssociation createdAssociation = ChurchMemberParishRoleAssociation.createChurchMemberParishRoleAssociation(churchMember, parishRole);
            churchMemberParishRoleAssociationRepository.save(createdAssociation);
        }


        // 구역 역할 검증 및 추가
        // TODO churchId 추가 finished
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getCellRoleId() != null) {
            // 요청한 cellRole 존재 검증
            CellRole cellRole = cellRoleRepository.findByChurchIdAndId(churchId, request.getCellRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CELL_ROLE));
            ChurchMemberCellRoleAssociation createdAssociation = ChurchMemberCellRoleAssociation.createChurchMemberCellRoleAssociation(churchMember, cellRole);
            churchMemberCellRoleAssociationRepository.save(createdAssociation);
        }

        // 회별 역할 검증 및 추가
        // TODO churchId 추가
        // TODO 가지고 있는 역할이 있는지 확인 먼저 해야함 있으면 수정 없다면 추가
        if (request.getGatheringRoleId() != null) {
            // gatheringRole 검증
            GatheringRole gatheringRole = gatheringRoleRepository.findByChurchIdAndId(churchId, request.getGatheringRoleId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GATHERING_ROLE));
            ChurchMemberGatheringRoleAssociation createdAssociation = ChurchMemberGatheringRoleAssociation.createChurchMemberGatheringRoleAssociation(churchMember, gatheringRole);
            churchMemberGatheringRoleAssociationRepository.save(createdAssociation);
        }

        // 봉사 및 봉사 역할 검증 및 추가
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        List<SaveChurchMemberRequest.MinistryDTO> ministries = objectMapper.readValue(request.getMinistries(), new TypeReference<List<SaveChurchMemberRequest.MinistryDTO>>() {
        });

        List<ChurchMemberMinistryAssociation> associations = new ArrayList<>();
        List<ChurchMemberMinistryRoleAssociation> roleAssociations = new ArrayList<>();

        for (SaveChurchMemberRequest.MinistryDTO ministryDTO: ministries) {
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
        // 벌크 save
        churchMemberMinistryAssociationRepository.saveAll(associations);
        churchMemberMinistryRoleAssociationRepository.saveAll(roleAssociations);


        // 파일이 있으면 업로드
        if (request.getProfileImage() != null) {
            // 파일 타입 검증
            String[] fileNames = request.getProfileImage().getOriginalFilename().split("\\.");
            String fileType = fileNames[fileNames.length - 1];

            boolean isAllowedExtension = false;

            for (String extension : ALLOWED_EXTENSIONS) {
                if (fileType.equalsIgnoreCase(extension)) {
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
        // 없다면 null로 설정
        else {
            churchMember.updateProfileImageName(null);
            churchMember.updateProfileImageThumbnail(null);
        }

        // TODO 성도 추가 시에는 플러쉬 해서 없는 ID 미리 받아오는 것 고려 / flush 안하고도 householdId와 자기 id를 맞춰줄수 있는 방법 있는지 확인
        // 정보 업데이트
        churchMembers.add(churchMember);
        // 커밋
        memberRepository.saveAll(churchMembers);

        return SaveChurchMemberResponse.from(churchMember.getId());
    }

    public ProfileImageUrlResponse getProfileImageUrl(Long churchId, Long memberId) {
        String profileImageUrl = profileImageService.getProfileImageUrl(churchId, memberId);
        return ProfileImageUrlResponse.builder()
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public DeleteChurchMemberResponse deleteChurchMember(Long churchId, Long churchMemberId) {
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(churchMemberId, churchId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHURCH_MEMBER));

        // 봉사 삭제
        List<ChurchMemberMinistryAssociation> ministryAssociations = churchMemberMinistryAssociationRepository.findAllByChurchMember(churchMember);
        if (!ministryAssociations.isEmpty()) {
            churchMemberMinistryAssociationRepository.deleteAllByChurchMemberId(churchMemberId);
        }

        // 봉사 역할 삭제
        List<ChurchMemberMinistryRoleAssociation> ministryRoleAssociations = churchMemberMinistryRoleAssociationRepository.findAllByChurchMember(churchMember);
        if (!ministryRoleAssociations.isEmpty()) {
            churchMemberMinistryRoleAssociationRepository.deleteAllByChurchMemberId(churchMemberId);
        }

        // 구역 역할 삭제
        ChurchMemberCellRoleAssociation cellRoleAssociation = churchMemberCellRoleAssociationRepository.findByChurchMember(churchMember);
        if (cellRoleAssociation != null) {
            churchMemberCellRoleAssociationRepository.delete(cellRoleAssociation);
        }

        // 회별 역할 삭제
        ChurchMemberGatheringRoleAssociation gatheringRoleAssociation = churchMemberGatheringRoleAssociationRepository.findByChurchMember(churchMember);
        if (gatheringRoleAssociation != null) {
            churchMemberGatheringRoleAssociationRepository.delete(gatheringRoleAssociation);
        }

        // 교구 역할 삭제
        ChurchMemberParishRoleAssociation parishRoleAssociation = churchMemberParishRoleAssociationRepository.findByChurchMember(churchMember);
        if (parishRoleAssociation != null) {
            churchMemberParishRoleAssociationRepository.delete(parishRoleAssociation);
        }

        //  TODO 자기 자신이 세대주일 경우 세대원의 householdId 자신으로 , 관계는 본인으로 변경
        if (churchMember.getId().equals(churchMember.getHouseholderId())) {
            Long householderId = churchMember.getHouseholderId();

            List<ChurchMember> churchMembers = memberRepository.findAllByChurchIdAndHouseholderId(churchId, householderId);
            for (ChurchMember member : churchMembers) {
                member.updateHouseholderId(member.getId());
                member.updateRelationshipWithHouseHolder("본인");
            }
            memberRepository.saveAll(churchMembers);
        }

        // auth 관계 제거
        Auth auth = authRepository.findByChurchMemberId(churchMemberId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        auth.setChurch(null);
        auth.setChurchMemberId(null);
        authRepository.save(auth);

        // 논리 삭제
        churchMember.setDeletedAt();
        memberRepository.save(churchMember);

        return DeleteChurchMemberResponse.from(churchMemberId);
    }
}
