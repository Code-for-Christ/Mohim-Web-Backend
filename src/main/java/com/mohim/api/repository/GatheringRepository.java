package com.mohim.api.repository;

import com.mohim.api.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
    List<Gathering> findByChurchId(Long church_id);

    Optional<Gathering> findByChurchIdAndId(Long churchId, Long id);
}
