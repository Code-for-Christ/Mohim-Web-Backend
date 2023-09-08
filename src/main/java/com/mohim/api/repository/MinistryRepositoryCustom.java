package com.mohim.api.repository;

import com.mohim.api.domain.Ministry;

import java.util.List;

public interface MinistryRepositoryCustom {
    List<Ministry> findByChurchIdAndMemberId(Integer churchId, Integer memberId);
}
