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
    List<ChurchMember> findAllByHouseholderId(Long householderId);
    @Query("SELECT cm FROM ChurchMember cm " +
            "INNER JOIN cm.churchMemberCellRoleAssociations cra " +
            "INNER JOIN cra.cellRole cr " +
            "WHERE cm.church.id = :churchId " +
            "AND cr.cell.id = :cellId " +
            "ORDER BY cr.ordinalPosition ASC")
    List<ChurchMember> findChurchMembersByCellIdAndChurchId(@Param("cellId") Long cellId, @Param("churchId") Long churchId);
}
