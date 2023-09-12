package com.mohim.api.service;

import com.mohim.api.repository.CellRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CellServiceTest {

    @Autowired
    CellService cellService;
    @Autowired
    CellRepository cellRepository;

    @Test
    @DisplayName("구역 목록 조회")
    void getCellList() {
        // given
        Long churchId = 1L;
        // then
        assertThat(cellService.getCellList(churchId).getCells().size()).isEqualTo(20);
    }

}