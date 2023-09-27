package com.mohim.api.repository;

import com.mohim.api.domain.ParishRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParishRoleRepository extends JpaRepository<ParishRole, Long> {
    @Query("SELECT pr FROM ParishRole pr " +
            "JOIN pr.parish p " +
            "JOIN p.church c " +
            "WHERE c.id = :churchId")
    List<ParishRole> findByChurchId(Long churchId);
}
