package com.mohim.api.repository;

import com.mohim.api.domain.GatheringRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GatheringRoleRepository extends JpaRepository<GatheringRole, Long> {
    @Query("SELECT gr FROM GatheringRole gr " +
            "JOIN gr.gathering g " +
            "JOIN g.church c " +
            "WHERE c.id = :churchId")
    List<GatheringRole> findByChurchId(Long churchId);
}
