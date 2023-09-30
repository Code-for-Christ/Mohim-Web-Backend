package com.mohim.api.repository;

import com.mohim.api.domain.ParishRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParishRoleRepository extends JpaRepository<ParishRole, Long> {
    @Query("SELECT pr FROM ParishRole pr " +
            "JOIN pr.parish p " +
            "JOIN p.church c " +
            "WHERE c.id = :churchId")
    List<ParishRole> findByChurchId(Long churchId);

    @Query("SELECT pr FROM ParishRole pr " +
            "JOIN pr.parish p " +
            "JOIN p.church c " +
            "WHERE c.id = :churchId AND pr.id = :id")
    Optional<ParishRole> findByChurchIdAndId(Long churchId, Long id);
}
