package com.mohim.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChurchMemberCellRoleAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "church_member_id")
    private ChurchMember churchMember;

    @ManyToOne
    @JoinColumn(name = "cell_role_id")
    private CellRole cellRole;
}
