package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<ChurchMember, Long>, MemberRepositoryCustom {

    public Optional<ChurchMember> findByNameAndPhoneNumber(String name, String phoneNumber);
}
