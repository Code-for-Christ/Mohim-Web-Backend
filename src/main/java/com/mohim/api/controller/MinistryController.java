package com.mohim.api.controller;

import com.mohim.api.dto.MinistriesResponse;
import com.mohim.api.service.MinistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/churches")
@RequiredArgsConstructor
public class MinistryController {

    private final MinistryService ministryService;

    @GetMapping("/{church_id}/ministries")
    public ResponseEntity<MinistriesResponse> getMinistryList(@PathVariable("church_id") Long churchId) {
        MinistriesResponse response = ministryService.getMinistryList(churchId);
        return ResponseEntity.ok().body(response);
    }

}
