package com.mohim.api.service;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.repository.MemberRepository;
import com.mohim.api.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberResponse> getList() {
        List<MemberResponse> members = memberRepository.findAll().stream()
                .map(churchMember -> new MemberResponse(churchMember))
                .collect(Collectors.toList());
        System.out.println(members.get(0).getName());
        return members;
    }

}
