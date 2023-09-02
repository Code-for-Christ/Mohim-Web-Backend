package com.mohim.api.repository;

import com.mohim.api.domain.Auth;
import com.mohim.api.domain.ChurchMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);
    Optional<Auth> findByChurchMemberId(Long churchMemberId);
}
