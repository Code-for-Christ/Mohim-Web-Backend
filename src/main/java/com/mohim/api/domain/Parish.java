package com.mohim.api.domain;

import javax.persistence.*;

@Entity
public class Parish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "church_id")
    private Church church;
}
