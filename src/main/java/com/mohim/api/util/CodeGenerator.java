package com.mohim.api.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CodeGenerator {

    public String generateEightCharacterCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").substring(0, 8);
    }
}