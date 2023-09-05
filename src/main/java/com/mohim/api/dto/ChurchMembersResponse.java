package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChurchMembersResponse {

    private Metadata metadata;
    private List<ChurchMember> churchMembers;

    @Builder
    @Getter
    public static class Metadata {
        private Integer totalCount;
        private Integer page;
        private Integer size;
        private List<String> orderBy;
        private String nextUrl;

        // 생성자, 게터 및 세터
    }

    @Getter
    @Builder
    public static class ChurchMember {
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
        private byte[] profileImageThumbnail;
        private Long churchId;
        private String churchName;
        private Long cellId;
        private Integer cell;
        private Long gatheringId;
        private String gatheringName;
        private Long positionId;
        private String positionName;
    }
}

