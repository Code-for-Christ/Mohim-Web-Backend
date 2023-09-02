package com.mohim.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Church {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    @Builder
    public Church(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Church createChurch(String name, String country) {
        return Church.builder()
                .name(name)
                .country(country)
                .build();
    }
}
