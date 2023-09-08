package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.ChurchMemberCellRoleAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChurchMemberCellRoleAssociationRepository extends JpaRepository<ChurchMemberCellRoleAssociation, Long> {
    ChurchMemberCellRoleAssociation findByChurchMember(ChurchMember churchMember);
}
