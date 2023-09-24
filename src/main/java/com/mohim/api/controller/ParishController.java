package com.mohim.api.controller;

import com.mohim.api.dto.ParishesResponse;
import com.mohim.api.service.ParishService;
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
public class ParishController {

    private final ParishService parishService;

    @GetMapping("/{church_id}/parishes")
    public ResponseEntity<ParishesResponse> getParishList(@PathVariable("church_id") Long churchId) {
        ParishesResponse response = parishService.getParishList(churchId);
        return ResponseEntity.ok().body(response);
    }
}
