package com.mohim.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChurchMemberParishRoleAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_member_id")
    private ChurchMember churchMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parish_role_id")
    private ParishRole parishRole;

    @Builder
    public ChurchMemberParishRoleAssociation(ChurchMember churchMember, ParishRole parishRole) {
        this.churchMember = churchMember;
        this.parishRole = parishRole;
    }

    public static ChurchMemberParishRoleAssociation createChurchMemberParishRoleAssociation(ChurchMember churchMember, ParishRole parishRole) {
        return ChurchMemberParishRoleAssociation.builder()
                .churchMember(churchMember)
                .parishRole(parishRole)
                .build();
    }

    public void updateParishRole(ParishRole parishRole) {
        this.parishRole = parishRole;
    }
}
