package com.mohim.api.service;

import com.mohim.api.dto.ParishesResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParishServiceTest {

    @Autowired
    ParishService parishService;

    @Test
    void getParishList() {
        // given
        Long churchId = 1L;

        // when
        ParishesResponse parishesResponse = parishService.getParishList(churchId);

        // then
        Assertions.assertThat(parishesResponse.getParishes().size()).isEqualTo(4);
    }
}