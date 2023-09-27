package com.mohim.api.controller;

import com.mohim.api.dto.CellLeadersResponse;
import com.mohim.api.dto.CellRolesResponse;
import com.mohim.api.dto.CellsResponse;
import com.mohim.api.service.CellService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/churches")
@Slf4j
@RequiredArgsConstructor
public class CellController {

    private final CellService cellService;

    // 구역 목록 조회
    @GetMapping("{church_id}/cells")
    public ResponseEntity<CellsResponse> getCellList(@PathVariable(name = "church_id") Long churchId) {
        CellsResponse response = cellService.getCellList(churchId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{church_id}/cells/{cell_id}/leaders")
    public ResponseEntity<CellLeadersResponse> getCellLeaders(@PathVariable(name = "church_id") Long churchId, @PathVariable(name = "cell_id") Long cellId) {
        CellLeadersResponse response = cellService.getCellLeaders(churchId, cellId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{church_id}/cell-roles")
    public ResponseEntity<CellRolesResponse> getCellRoles(@PathVariable("church_id") Long churchId) {
        CellRolesResponse response = cellService.getCellRoles(churchId);
        return ResponseEntity.ok().body(response);
    }
}
