package com.mohim.api.response;

import com.mohim.api.domain.ChurchMember;
import lombok.Builder;
import lombok.Getter;


@Getter
public class MemberResponse {
    private final Long id;

    private final String name;

//    private Long churchId;
//
//    private Long cellId;
//
//    private Long gatheringId;
//
//    private Long positionId;
//
//    private Long householderId;
//
//    private Long relationshipWithHouseHolder;
//
//    private Integer birthYear;
//
//    private Integer salvationYear;
//
//    private Integer salvationMonth;
//
//    private Integer salvationDay;
//
//    private String carNumber;
//
//    private Gender gender;

    private final String phoneNumber;

    private final String address;

//    private String profileImageName;
//
//    private byte[] profileImageThumbnail;

    public MemberResponse(ChurchMember churchMember) {
        this.id = churchMember.getId();
        this.name = churchMember.getName();
        this.phoneNumber = churchMember.getPhoneNumber();
        this.address = churchMember.getAddress();
    }


    @Builder
    public MemberResponse(Long id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
