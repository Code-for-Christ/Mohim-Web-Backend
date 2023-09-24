package com.mohim.api.repository;

import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.ChurchMemberResponse;
import com.mohim.api.dto.ChurchMembersRequest;

import java.util.List;

public interface MemberRepositoryCustom {
    List<ChurchMember> findByChurchId(Integer churchId, ChurchMembersRequest request);
    Integer getTotalCount(Integer churchId, ChurchMembersRequest request);
    ChurchMemberResponse findByMemberId(Long memberId, Long churchId);
}
