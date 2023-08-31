package com.mohim.api.repository;

import com.mohim.api.domain.Church;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChurchRepository extends JpaRepository<Church, Long> {
}
