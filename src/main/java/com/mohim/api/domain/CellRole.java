package com.mohim.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CellRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cell_id")
    private Cell cell;

    private String role;

    @Column(name = "ordinal_position")
    private Integer ordinalPosition;

    @Builder
    public CellRole(Cell cell, String role, Integer ordinalPosition) {
        this.cell = cell;
        this.role = role;
        this.ordinalPosition = ordinalPosition;
    }

    public static CellRole createCellRole(Cell cell, String role, Integer ordinalPosition) {
        return CellRole.builder()
                .cell(cell)
                .role(role)
                .ordinalPosition(ordinalPosition)
                .build();
    }
}
