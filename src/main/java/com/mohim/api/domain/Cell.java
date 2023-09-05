package com.mohim.api.domain;

import javax.persistence.*;

@Entity
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
}
