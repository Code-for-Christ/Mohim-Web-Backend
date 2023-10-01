package com.mohim.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChurchMemberMinistryRoleAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "church_member_id")
    private ChurchMember churchMember;

    @ManyToOne
    @JoinColumn(name = "ministry_role_id")
    private MinistryRole ministryRole;

    @Builder
    public ChurchMemberMinistryRoleAssociation(ChurchMember churchMember, MinistryRole ministryRole) {
        this.churchMember = churchMember;
        this.ministryRole = ministryRole;
    }

    public static ChurchMemberMinistryRoleAssociation createChurchMemberMinistryRoleAssociation(ChurchMember churchMember, MinistryRole ministryRole) {
        return ChurchMemberMinistryRoleAssociation.builder()
                .churchMember(churchMember)
                .ministryRole(ministryRole)
                .build();
    }
}
