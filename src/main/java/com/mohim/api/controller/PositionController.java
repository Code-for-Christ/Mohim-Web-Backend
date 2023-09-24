package com.mohim.api.controller;

import com.mohim.api.dto.PositionsResponse;
import com.mohim.api.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/churches")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/{church_id}/positions")
    public ResponseEntity<PositionsResponse> getPositionList(@PathVariable("church_id") Long churchId) {
        PositionsResponse response = positionService.getPositionList(churchId);
        return ResponseEntity.ok().body(response);
    }
}
