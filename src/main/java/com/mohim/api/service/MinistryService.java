package com.mohim.api.service;

import com.mohim.api.dto.MinistriesResponse;
import com.mohim.api.dto.MinistryDTO;
import com.mohim.api.dto.MinistryLeaderDTO;
import com.mohim.api.dto.MinistryLeadersResponse;
import com.mohim.api.mapper.MinistryMapper;
import com.mohim.api.repository.MemberRepository;
import com.mohim.api.repository.MinistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinistryService {

    private final MinistryRepository ministryRepository;
    private final MinistryMapper ministryMapper;
    private final MemberRepository memberRepository;

    public MinistriesResponse getMinistryList(Long churchId) {
        List<MinistryDTO> ministryDTOS = ministryRepository.findByChurchId(churchId).stream()
                .map(ministry -> {
                    return ministryMapper.toMinistryDTO(ministry);
                }).collect(Collectors.toList());
        return ministryMapper.toMinistryResponse(ministryDTOS);
    }

    @Transactional(readOnly = true)
    public MinistryLeadersResponse getMinistryLeaders(Long churchId, Long ministryId) {
        List<MinistryLeaderDTO> ministryLeaderDTOS = memberRepository.findChurchMembersByMinistryIdAndChurchId(ministryId, churchId).stream()
                .map(churchMember -> {
                    return ministryMapper.toMinistryLeaderDTO(churchMember);
                }).collect(Collectors.toList());
        return ministryMapper.toMinistryLeadersResponse(ministryLeaderDTOS);
    }
}
