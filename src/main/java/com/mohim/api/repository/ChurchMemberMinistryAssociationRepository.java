package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.domain.ChurchMemberMinistryAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChurchMemberMinistryAssociationRepository extends JpaRepository<ChurchMemberMinistryAssociation, Long> {
    List<ChurchMemberMinistryAssociation> findAllByChurchMember(ChurchMember churchMember);
}
