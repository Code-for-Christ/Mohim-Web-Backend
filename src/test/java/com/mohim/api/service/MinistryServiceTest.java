package com.mohim.api.service;

import com.mohim.api.dto.MinistriesResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MinistryServiceTest {

    @Autowired
    MinistryService ministryService;

    @Test
    @DisplayName("회별 목록 조회")
    void getMinistryList() {
        //given
        Long churchId = 1L;

        // when
        MinistriesResponse ministryList = ministryService.getMinistryList(churchId);

        // then
        Assertions.assertThat(ministryList.getMinistries().size()).isEqualTo(15);
    }
}