package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.ChurchMemberGatheringRoleAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChurchMemberGatheringRoleAssociationRepository extends JpaRepository<ChurchMemberGatheringRoleAssociation, Long> {
    ChurchMemberGatheringRoleAssociation findByChurchMember(ChurchMember churchMember);
}
