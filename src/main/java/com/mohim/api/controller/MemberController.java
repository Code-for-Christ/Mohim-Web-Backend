package com.mohim.api.controller;

import com.mohim.api.dto.ChurchMembersRequest;
import com.mohim.api.dto.ChurchMembersResponse;
import com.mohim.api.response.MemberResponse;
import com.mohim.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{church_id}/members")
    public ResponseEntity<ChurchMembersResponse> getChurchMembers(@PathVariable("church_id") Integer churchId, @ModelAttribute ChurchMembersRequest request, HttpServletRequest httpServletRequest) {
        System.out.println("request = " + request.getSize());
        System.out.println("request.getCellId() = " + request.getCellId());
        ChurchMembersResponse response = memberService.getChurchMembers(churchId, request, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

}
