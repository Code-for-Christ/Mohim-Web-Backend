package com.mohim.api.repository;

import com.mohim.api.domain.Ministry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MinistryRepository extends JpaRepository<Ministry, Long>, MinistryRepositoryCustom {
    List<Ministry> findByChurchId(Long church_id);
}
