package com.mohim.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChurchMemberMinistryAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_member_id")
    private ChurchMember churchMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id")
    private Ministry ministry;

    @Builder
    public ChurchMemberMinistryAssociation(ChurchMember churchMember, Ministry ministry) {
        this.churchMember = churchMember;
        this.ministry = ministry;
    }

    public static ChurchMemberMinistryAssociation createChurchMemberMinistryAssociation(ChurchMember churchMember, Ministry ministry) {
        return ChurchMemberMinistryAssociation.builder()
                .churchMember(churchMember)
                .ministry(ministry)
                .build();
    }
}
