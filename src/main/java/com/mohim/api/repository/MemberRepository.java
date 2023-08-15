package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<ChurchMember, Long> {
}
