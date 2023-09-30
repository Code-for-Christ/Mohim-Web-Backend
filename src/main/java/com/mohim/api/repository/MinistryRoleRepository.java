package com.mohim.api.repository;

import com.mohim.api.domain.MinistryRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MinistryRoleRepository extends JpaRepository<MinistryRole, Long> {
    @Query("SELECT mr FROM MinistryRole mr " +
            "JOIN mr.ministry m " +
            "JOIN m.church c " +
            "WHERE c.id = :churchId")
    List<MinistryRole> findByChurchId(Long churchId);

    @Query("SELECT mr FROM MinistryRole mr " +
            "JOIN mr.ministry m " +
            "JOIN m.church c " +
            "WHERE c.id = :churchId AND mr.id = :id")
    Optional<MinistryRole> findByChurchIdAndId(Long churchId, Long id);

}
