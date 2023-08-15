package com.mohim.api.controller;

import com.mohim.api.repository.MemberRepository;
import com.mohim.api.response.MemberResponse;
import com.mohim.api.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("교회 명단을 가져온다.")
    void getChurchMembers() throws Exception{
        System.out.println("asdf");
    }
}