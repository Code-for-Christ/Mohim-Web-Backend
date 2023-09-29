package com.mohim.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public ChurchMemberCellRoleAssociation(ChurchMember churchMember, CellRole cellRole) {
        this.churchMember = churchMember;
        this.cellRole = cellRole;
    }

    public static ChurchMemberCellRoleAssociation createChurchMemberCellRoleAssociation(ChurchMember churchMember, CellRole cellRole) {
        return ChurchMemberCellRoleAssociation.builder()
                .churchMember(churchMember)
                .cellRole(cellRole)
                .build();
    }

}
