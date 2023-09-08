package com.mohim.api.domain;

import lombok.AccessLevel;
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
}
