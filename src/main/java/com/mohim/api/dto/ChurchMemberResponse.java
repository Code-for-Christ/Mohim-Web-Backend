package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ChurchMemberResponse {
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
    private Integer parish;
    private Long parishRoleId;
    private String parishRole;
    private Long cellRoleId;
    private String cellRole;
    private Long gatheringRoleId;
    private String gatheringRole;
    private List<Household> household;
    private List<Ministry> ministries;

    @Builder
    @Getter
    public static class Household {
        private Long id;
        private String name;
        private Long householderId;
        private String relationshipWithHouseholder;
        private String phoneNumber;
        private String profileImageThumbnail;
    }

    @Builder
    @Getter
    @Setter
    public static class Ministry {
        private Long id;
        private String name;
        private Long roleId;
        private String role;
    }
}
