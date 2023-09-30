package com.mohim.api.repository;

import com.mohim.api.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByChurchId(Long church_id);

    Optional<Position> findByChurchIdAndId(Long church_id, Long id);
}
