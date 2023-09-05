package com.mohim.api.service;

import com.mohim.api.domain.Church;
import com.mohim.api.dto.ChurchMembersRequest;
import com.mohim.api.dto.ChurchMembersResponse;
import com.mohim.api.dto.ChurchResponse;
import com.mohim.api.repository.ChurchRepository;
import com.mohim.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChurchService {

    private final ChurchRepository churchRepository;
    private final MemberRepository memberRepository;

    public List<ChurchResponse> getChurchList() {

        List<Church> churches = churchRepository.findAll();

        return churches.stream()
                .map(church -> ChurchResponse.builder()
                        .name(church.getName())
                        .id(church.getId())
                        .country(church.getCountry())
                        .build())
                .collect(Collectors.toList());
    }

    public ChurchMembersResponse getChurchMembers(Integer churchId, ChurchMembersRequest request, HttpServletRequest httpServletRequest) {
        List<ChurchMembersResponse.ChurchMember> churchMembers = memberRepository.findByChurchId(churchId, request).stream()
                .map(churchMember -> ChurchMembersResponse.ChurchMember.builder()
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
                        .cell(churchMember.getCell().getCell())
                        .cellId(churchMember.getCell().getId())
                        .gatheringId(churchMember.getGathering().getId())
                        .gatheringName(churchMember.getGathering().getName())
                        .positionId(churchMember.getPosition() == null? null : churchMember.getPosition().getId())
                        .positionName(churchMember.getPosition() == null? null : churchMember.getPosition().getName())
                        .build())
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
}
