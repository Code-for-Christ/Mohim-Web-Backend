package com.mohim.api.service;

import com.mohim.api.dto.PositionsResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PositionServiceTest {

    @Autowired
    PositionService positionService;

    @Test
    void getPositionList() {
        // given
        Long churchId = 1L;

        // when
        PositionsResponse positionsResponse = positionService.getPositionList(churchId);

        // then
        Assertions.assertThat(positionsResponse.getPositions().size()).isEqualTo(5);
    }
}