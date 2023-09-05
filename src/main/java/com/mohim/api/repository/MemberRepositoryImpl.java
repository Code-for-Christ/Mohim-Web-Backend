package com.mohim.api.repository;

import com.mohim.api.domain.*;
import com.mohim.api.dto.ChurchMembersRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChurchMember> findByChurchId(Integer churchId, ChurchMembersRequest request) {
        // 필요 QEntity 임포트
        QChurch church = QChurch.church;
        QChurchMember churchMember = QChurchMember.churchMember;
        QCell cell = QCell.cell1;
        QGathering gathering = QGathering.gathering;
        QPosition position = QPosition.position;

        // 동적 쿼리 설정
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(churchMember.church.id.eq(Long.valueOf(churchId)));

        if (request.getMemberId() != null){
            builder.and(churchMember.id.eq(Long.valueOf(request.getMemberId())));
        }
        if (request.getHouseholderId() != null) {
            builder.and(churchMember.householderId.eq(Long.valueOf(request.getHouseholderId())));
        }
        if (request.getParish() != null) {
            builder.and(cell.parish.id.eq(Long.valueOf(request.getParish())));
        }
        if (request.getCellId() != null) {
            builder.and(churchMember.cell.id.eq(Long.valueOf(request.getCellId())));
        }
        if (request.getGatheringId() != null) {
            builder.and(churchMember.gathering.id.eq(Long.valueOf(request.getGatheringId())));
        }
        if (request.getPositionId() != null) {
            builder.and(churchMember.position.id.eq(Long.valueOf(request.getPositionId())));
        }
        if (request.getSearch() != null) {
             builder.and(churchMember.name.contains(request.getSearch()))
                     .or(churchMember.phoneNumber.contains(request.getSearch()))
                     .or(churchMember.carNumber.contains(request.getSearch()));
        }

        JPAQuery<ChurchMember> query = jpaQueryFactory.selectFrom(churchMember)
                .where(builder)
                .innerJoin(churchMember.church, church)
                .innerJoin(churchMember.cell, cell)
                .innerJoin(churchMember.gathering, gathering)
                .leftJoin(churchMember.position, position);


        // 동적 정렬 설정
        PathBuilder<?> churchMemberPath = new PathBuilder<>(ChurchMember.class, "churchMember");

        if (!request.getOrderBy().isEmpty()) {
            List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

            int maxOrderByFields = Math.min(request.getOrderBy().size(), 3); // 최대 3개 필드까지 선택 가능
            for (int i = 0; i < maxOrderByFields; i++) {
                String orderByField = request.getOrderBy().get(i);
                OrderSpecifier<?> orderSpecifier = getOrderBySpecifier(churchMemberPath, orderByField);
                if (orderSpecifier != null) {
                    orderSpecifiers.add(orderSpecifier);
                }
            }
            query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        } else {
            // 기본 정렬: "name"
            query.orderBy(churchMember.name.asc());
        }


        return query
                .offset((long) (request.getPage() - 1) * request.getSize())
                .limit(request.getSize())
                .fetch();
    }

    @Override
    public Integer getTotalCount(Integer churchId, ChurchMembersRequest request) {
        // 필요 QEntity 임포트
        QChurch church = QChurch.church;
        QChurchMember churchMember = QChurchMember.churchMember;
        QCell cell = QCell.cell1;
        QGathering gathering = QGathering.gathering;
        QPosition position = QPosition.position;

        // 동적 쿼리 설정
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(churchMember.church.id.eq(Long.valueOf(churchId)));

        if (request.getMemberId() != null){
            builder.and(churchMember.id.eq(Long.valueOf(request.getMemberId())));
        }
        if (request.getHouseholderId() != null) {
            builder.and(churchMember.householderId.eq(Long.valueOf(request.getHouseholderId())));
        }
        if (request.getParish() != null) {
            builder.and(cell.parish.id.eq(Long.valueOf(request.getParish())));
        }
        if (request.getCellId() != null) {
            builder.and(churchMember.cell.id.eq(Long.valueOf(request.getCellId())));
        }
        if (request.getGatheringId() != null) {
            builder.and(churchMember.gathering.id.eq(Long.valueOf(request.getGatheringId())));
        }
        if (request.getPositionId() != null) {
            builder.and(churchMember.position.id.eq(Long.valueOf(request.getPositionId())));
        }
        if (request.getSearch() != null) {
            builder.and(churchMember.name.contains(request.getSearch()))
                    .or(churchMember.phoneNumber.contains(request.getSearch()))
                    .or(churchMember.carNumber.contains(request.getSearch()));
        }

        JPAQuery<ChurchMember> query = jpaQueryFactory.selectFrom(churchMember)
                .where(builder)
                .innerJoin(churchMember.church, church)
                .innerJoin(churchMember.cell, cell)
                .innerJoin(churchMember.gathering, gathering)
                .leftJoin(churchMember.position, position);


        return query.fetch().size();
    }

    private OrderSpecifier<?> getOrderBySpecifier(PathBuilder<?> churchMemberPath, String orderByField) {
        switch (orderByField) {
            case "id":
                return new OrderSpecifier<>(Order.ASC, churchMemberPath.getString("id"));
            case "name":
                return new OrderSpecifier<>(Order.ASC, churchMemberPath.getString("name"));
            case "household_id":
                return new OrderSpecifier<>(Order.ASC, churchMemberPath.getString("householderId"));
            default:
                return null;
        }
    }
}








