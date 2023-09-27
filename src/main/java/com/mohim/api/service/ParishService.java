package com.mohim.api.service;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.*;
import com.mohim.api.repository.MemberRepository;
import com.mohim.api.repository.ParishRepository;
import com.mohim.api.repository.ParishRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParishService {

    private final ParishRepository parishRepository;
    private final MemberRepository memberRepository;
    private final ParishRoleRepository parishRoleRepository;

    public ParishesResponse getParishList(Long churchId) {
        List<Integer> parishes = parishRepository.findByChurchId(churchId).stream()
                .map(parish -> parish.getId().intValue())
                .collect(Collectors.toList());
        return ParishesResponse.from(parishes);
    }

    public ParishLeadersResponse getParishLeaders(Long churchId, Long parish) {
        List<ParishLeaderDTO> parishLeaderDTOS = memberRepository.findChurchMembersByParishIdAndChurchId(parish, churchId).stream()
                .map(churchMember -> ParishLeaderDTO.from(churchMember))
                .collect(Collectors.toList());

        return ParishLeadersResponse.from(parishLeaderDTOS);
    }

    public ParishRolesResponse getParishRoles(Long churchId) {
        List<ParishRoleDTO> parishRoleDTOS = parishRoleRepository.findByChurchId(churchId).stream()
                .map(parishRole -> ParishRoleDTO.from(parishRole))
                .collect(Collectors.toList());

        return ParishRolesResponse.from(parishRoleDTOS);
    }
}
