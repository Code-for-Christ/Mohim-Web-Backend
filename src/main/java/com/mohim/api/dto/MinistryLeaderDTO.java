package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MinistryLeaderDTO {
    private Long id;
    private String name;
    private Long householderId;
    private String relationshipWithHouseholder;
    private Integer birthYear;
    private Integer salvationYear;
    private Integer salvationMonth;
    private Integer salvationDay;
    private String carNumber;
    private String sex;
    private String phoneNumber;
    private String address;
    private String profileImageThumbnail;
    private Long churchId;
    private String churchName;
    private Long cellId;
    private Integer cell;
    private Long gatheringId;
    private String gatheringName;
    private Long positionId;
    private String positionName;
    private Long ministryId;
    private String ministryName;
    private String ministryRole;
}
