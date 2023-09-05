package com.mohim.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parish_id")
    private Parish parish;

    private Integer cell;

    @ManyToOne
    @JoinColumn(name = "church_id")
    private Church church;

    @Builder
    public Cell(Parish parish, Integer cell, Church church) {
        this.parish = parish;
        this.cell = cell;
        this.church = church;
    }

    public static Cell createCell(Parish parish, Integer cell, Church church) {
        return Cell.builder()
                .parish(parish)
                .cell(cell)
                .church(church)
                .build();
    }
}
