package com.mohim.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChurchMemberRequest {
    @NotNull
    private String name;
    @NotNull
    private String sex;
    private String phoneNumber;
    private String carNumber;
    private Integer birthYear;
    private Integer salvationYear;
    private Integer salvationMonth;
    private Integer salvationDay;
    private MultipartFile profileImage;
    @NotNull
    private Long householderId;
    @NotNull
    private String relationshipWithHouseholder;
    private String address;
    @NotNull
    private Boolean updateHouseholdAddress;
    @NotNull
    private Long parish;
    private Long parishRoleId;
    @NotNull
    private Long cellId;
    private Long cellRoleId;
    @NotNull
    private Long gatheringId;
    private Long gatheringRoleId;
    private Long positionId;
    private String ministries;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MinistryDTO {
        public Long ministryId;
        public Long ministryRoleId;
    }
}
