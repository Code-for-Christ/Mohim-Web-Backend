package com.mohim.api.controller;

import com.mohim.api.dto.CellRolesResponse;
import com.mohim.api.service.CellService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CellControllerTest {
    @Autowired
    CellService cellService;
    @Test
    void getCellRoles() {
        // given
        Long churchId = 1L;

        // when
        CellRolesResponse response = cellService.getCellRoles(churchId);

        // then
        Assertions.assertThat(response.getCellRoles().size()).isEqualTo(80);
    }
}