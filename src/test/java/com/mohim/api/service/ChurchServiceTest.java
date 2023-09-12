package com.mohim.api.service;

import com.mohim.api.dto.ChurchesResponse;
import com.mohim.api.repository.ChurchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChurchServiceTest {

    @Autowired
    ChurchRepository churchRepository;

    @Autowired
    ChurchService churchService;


    @Test
    @DisplayName("교회 리스트 가져오기")
    void getChurchList() {
        ChurchesResponse churches = churchService.getChurchList();

        assertThat(churches.getChurches().size()).isEqualTo(1);

        System.out.println("churches.get(0).getName() = " + churches.getChurches().get(0).getName());
    }
}