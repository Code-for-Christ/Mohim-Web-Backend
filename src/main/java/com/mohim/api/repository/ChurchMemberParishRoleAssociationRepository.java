package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.ChurchMemberParishRoleAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChurchMemberParishRoleAssociationRepository extends JpaRepository<ChurchMemberParishRoleAssociation, Long> {
    ChurchMemberParishRoleAssociation findByChurchMember(ChurchMember churchMember);
}
