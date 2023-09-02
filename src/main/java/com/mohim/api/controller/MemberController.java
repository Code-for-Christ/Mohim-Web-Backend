package com.mohim.api.controller;

import com.mohim.api.response.MemberResponse;
import com.mohim.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponse> getChurchMembers() {
        return memberService.getList();
    }

}
