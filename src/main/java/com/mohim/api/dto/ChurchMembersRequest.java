package com.mohim.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChurchMembersRequest {
    Integer memberId;
    Integer householderId;
    Integer parish;
    Integer cellId;
    Integer gatheringId;
    Integer positionId;
//    OrderBy orderBy = OrderBy.NAME;
    List<String> orderBy = new ArrayList<>(List.of("name"));
    Integer page = 1;
    Integer size = 20;
    String search;
}
