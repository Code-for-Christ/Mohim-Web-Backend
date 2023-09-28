package com.mohim.api.service;

import com.mohim.api.domain.Gathering;
import com.mohim.api.dto.*;
import com.mohim.api.mapper.GatheringMapper;
import com.mohim.api.repository.GatheringRepository;
import com.mohim.api.repository.GatheringRoleRepository;
import com.mohim.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;
    private final GatheringMapper gatheringMapper;
    private final GatheringRoleRepository gatheringRoleRepository;

    public GatheringsResponse getGatheringList(Long churchId) {
        List<GatheringDTO> gatheringDTOS = gatheringRepository.findByChurchId(churchId).stream()
                .map(gathering -> {
                    return gatheringMapper.toGatheringDTO(gathering);
                })
                .collect(Collectors.toList());

        return gatheringMapper.toGatheringsResponse(gatheringDTOS);
    }

    public GatheringLeadersResponse getGatheringLeaders(Long churchId, Long gatheringId) {
        List<GatheringLeaderDTO> gatheringLeaderDTOS = memberRepository.findChurchMembersByGatheringIdAndChurchId(gatheringId, churchId).stream()
                .map(churchMember -> {
                    return gatheringMapper.toGatheringLeaderDTO(churchMember);
                })
                .collect(Collectors.toList());

        return gatheringMapper.toGatheringLeadersResponse(gatheringLeaderDTOS);
    }

    public GatheringRolesResponse getGatheringRoles(Long churchId) {
        List<GatheringRoleDTO> gatheringRoleDTOS = gatheringRoleRepository.findByChurchId(churchId).stream()
                .map(gatheringRole -> GatheringRoleDTO.from(gatheringRole))
                .collect(Collectors.toList());

        return GatheringRolesResponse.from(gatheringRoleDTOS);
    }
}
