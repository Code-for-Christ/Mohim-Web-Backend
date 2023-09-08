package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.ChurchMemberMinistryRoleAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChurchMemberMinistryRoleAssociationRepository extends JpaRepository<ChurchMemberMinistryRoleAssociation, Long> {
    List<ChurchMemberMinistryRoleAssociation> findAllByChurchMember(ChurchMember churchMember);
}
