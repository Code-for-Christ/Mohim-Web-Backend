package com.mohim.api.service;

import com.mohim.api.dto.GatheringRolesResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GatheringServiceTest {
    @Autowired
    GatheringService gatheringService;
    @Test
    void getGatheringRoles() {
        // given
        Long churchId = 1L;

        // when
        GatheringRolesResponse response = gatheringService.getGatheringRoles(churchId);

        // then
        Assertions.assertThat(response.getGatheringRoles().size()).isEqualTo(15);
    }
}