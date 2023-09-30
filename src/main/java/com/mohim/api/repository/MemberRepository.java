package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<ChurchMember, Long>, MemberRepositoryCustom {

    Optional<ChurchMember> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<ChurchMember> findByIdAndChurchId(Long id, Long churchId);
    List<ChurchMember> findAllByChurchIdAndHouseholderId(Long church_id, Long householderId);
    @Query("SELECT cm FROM ChurchMember cm " +
            "INNER JOIN cm.churchMemberCellRoleAssociations cra " +
            "INNER JOIN cra.cellRole cr " +
            "WHERE cm.church.id = :churchId " +
            "AND cr.cell.id = :cellId " +
            "ORDER BY cr.ordinalPosition ASC")
    List<ChurchMember> findChurchMembersByCellIdAndChurchId(@Param("cellId") Long cellId, @Param("churchId") Long churchId);

    @Query("SELECT cm FROM ChurchMember cm " +
            "INNER JOIN cm.churchMemberGatheringRoleAssociations gra " +
            "INNER JOIN gra.gatheringRole gr " +
            "WHERE cm.church.id = :churchId " +
            "AND gr.gathering.id = :gatheringId " +
            "ORDER BY gr.ordinalPosition ASC")
    List<ChurchMember> findChurchMembersByGatheringIdAndChurchId(
            @Param("gatheringId") Long gatheringId, @Param("churchId") Long churchId
    );

    @Query("SELECT cm FROM ChurchMember cm " +
            "INNER JOIN cm.churchMemberMinistryRoleAssociations mra " +
            "INNER JOIN mra.ministryRole mr " +
            "WHERE cm.church.id = :churchId " +
            "AND mr.ministry.id = :ministryId " +
            "ORDER BY mr.ordinalPosition ASC")
    List<ChurchMember> findChurchMembersByMinistryIdAndChurchId(
            @Param("ministryId") Long ministryId, @Param("churchId") Long churchId
    );

    @Query("SELECT cm FROM ChurchMember cm " +
            "INNER JOIN cm.churchMemberParishRoleAssociations mra " +
            "INNER JOIN mra.parishRole mr " +
            "WHERE cm.church.id = :churchId " +
            "AND mr.parish.id = :parishId " +
            "ORDER BY mr.ordinalPosition ASC")
    List<ChurchMember> findChurchMembersByParishIdAndChurchId(
            @Param("parishId") Long parishId, @Param("churchId") Long churchId
    );
}
