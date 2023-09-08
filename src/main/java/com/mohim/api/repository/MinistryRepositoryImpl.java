package com.mohim.api.repository;

import com.mohim.api.domain.Ministry;
import com.mohim.api.domain.QChurchMemberMinistryAssociation;
import com.mohim.api.domain.QMinistry;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MinistryRepositoryImpl implements MinistryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Ministry> findByChurchIdAndMemberId(Integer churchId, Integer memberId) {
        QMinistry ministry = QMinistry.ministry;
        QChurchMemberMinistryAssociation churchMemberMinistryAssociation = QChurchMemberMinistryAssociation.churchMemberMinistryAssociation;

        return null;
    }
}
