package com.mohim.api.repository;

import com.mohim.api.domain.CellRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CellRoleRepository extends JpaRepository<CellRole, Long> {
    @Query("SELECT cr FROM CellRole cr " +
            "JOIN cr.cell c " +
            "JOIN c.church ch " +
            "WHERE ch.id = :churchId")
    List<CellRole> findByChurchId(Long churchId);

    @Query("SELECT cr FROM CellRole cr " +
            "JOIN cr.cell c " +
            "JOIN c.church ch " +
            "WHERE ch.id = :churchId AND cr.id = :id")
    Optional<CellRole> findByChurchIdAndId(Long churchId, Long id);
}
