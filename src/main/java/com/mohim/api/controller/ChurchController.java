package com.mohim.api.controller;

import com.mohim.api.dto.ChurchesResponse;
import com.mohim.api.service.ChurchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/churches")
@Slf4j
@RequiredArgsConstructor
public class ChurchController {

    private final ChurchService churchService;

    // 교회 리스트 가져오기
    @GetMapping("")
    public ResponseEntity<ChurchesResponse> getChurchList() {
        ChurchesResponse response = churchService.getChurchList();
        return ResponseEntity.ok().body(response);
    }
}
