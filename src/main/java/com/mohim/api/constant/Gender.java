package com.mohim.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    MAIL("male"),
    FEMAIL("female");

    private String value;

    public static Gender fromCode(String dbData) {
        return Arrays.stream(Gender.values()).filter(v -> v.getValue().equals(dbData)).findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%가 존재하지 않음", dbData)));
    }

}
