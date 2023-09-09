package com.mohim.api.repository;

import com.mohim.api.domain.Cell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CellRepository extends JpaRepository<Cell, Long> {
    List<Cell> findAllByChurchId(Long church_id);
}
