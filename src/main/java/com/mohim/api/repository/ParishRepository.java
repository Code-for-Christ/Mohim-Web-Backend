package com.mohim.api.repository;

import com.mohim.api.domain.Parish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParishRepository extends JpaRepository<Parish, Long> {
    List<Parish> findByChurchId(Long church_id);

    Optional<Parish> findByChurchIdAndId(Long churchId, Long id);
}
