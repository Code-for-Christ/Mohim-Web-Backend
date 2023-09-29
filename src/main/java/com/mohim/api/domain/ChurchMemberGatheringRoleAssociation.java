package com.mohim.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChurchMemberGatheringRoleAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "church_member_id")
    private ChurchMember churchMember;

    @ManyToOne
    @JoinColumn(name = "gathering_role_id")
    private GatheringRole gatheringRole;

    @Builder
    public ChurchMemberGatheringRoleAssociation(ChurchMember churchMember, GatheringRole gatheringRole) {
        this.churchMember = churchMember;
        this.gatheringRole = gatheringRole;
    }

    public static ChurchMemberGatheringRoleAssociation createChurchMemberGatheringRoleAssociation(ChurchMember churchMember, GatheringRole gatheringRole) {
        return ChurchMemberGatheringRoleAssociation.builder()
                .churchMember(churchMember)
                .gatheringRole(gatheringRole)
                .build();
    }
}
