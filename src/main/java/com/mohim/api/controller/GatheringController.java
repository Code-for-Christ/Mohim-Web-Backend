package com.mohim.api.controller;

import com.mohim.api.dto.GatheringDTO;
import com.mohim.api.dto.GatheringsResponse;
import com.mohim.api.service.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/churches")
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @GetMapping("/{church_id}/gatherings")
    public ResponseEntity<GatheringsResponse> getGatheringList(@PathVariable("church_id") Long churchId){
        GatheringsResponse response = gatheringService.getGatheringList(churchId);
        return ResponseEntity.ok().body(response);
    }
}
