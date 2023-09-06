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
}
