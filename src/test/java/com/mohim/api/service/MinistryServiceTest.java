package com.mohim.api.service;

import com.mohim.api.dto.MinistriesResponse;
import com.mohim.api.dto.MinistryLeadersResponse;
import com.mohim.api.dto.MinistryRolesResponse;
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

    @Test
    @DisplayName("회별 임원 목록 조회")
    void getMinistryLeaders() {
        //given
        Long churchId = 1L;
        Long ministryId = 1L;

        // when
        MinistryLeadersResponse ministryLeaders = ministryService.getMinistryLeaders(churchId, ministryId);

        // then
        Assertions.assertThat(ministryLeaders.getChurchMembers().get(0).getMinistryName()).isEqualTo("교회학교");
    }

    @Test
    @DisplayName("봉사 역할 조회")
    void getMinistryRoles() {
        //given
        Long churchId = 1L;

        // when
        MinistryRolesResponse response = ministryService.getMinistryRoles(churchId);

        // then
        Assertions.assertThat(response.getMinistryRoles().size()).isEqualTo(49);
    }
}