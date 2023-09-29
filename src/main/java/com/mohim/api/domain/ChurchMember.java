package com.mohim.api.domain;

import com.mohim.api.constant.Gender;
import com.mohim.api.converter.GenderConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChurchMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "church_id")
    private Church church;

    @ManyToOne
    @JoinColumn(name = "cell_id")
    private Cell cell;

    @ManyToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "householder_id")
    private Long householderId;

    @Column(name = "relationship_with_householder")
    private String relationshipWithHouseHolder;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "salvation_year")
    private Integer salvationYear;

    @Column(name = "salvation_month")
    private Integer salvationMonth;

    @Column(name = "salvation_day")
    private Integer salvationDay;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "sex")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    @Column(name = "profile_image_name")
    private String profileImageName;

    @Column(name = "profile_image_thumbnail")
    private String profileImageThumbnail;

    @OneToMany(mappedBy = "churchMember")
    private List<ChurchMemberCellRoleAssociation> churchMemberCellRoleAssociations;

    @OneToMany(mappedBy = "churchMember")
    private List<ChurchMemberGatheringRoleAssociation> churchMemberGatheringRoleAssociations;

    @OneToMany(mappedBy = "churchMember")
    private List<ChurchMemberMinistryRoleAssociation> churchMemberMinistryRoleAssociations;

    @OneToMany(mappedBy = "churchMember")
    private List<ChurchMemberParishRoleAssociation> churchMemberParishRoleAssociations;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now();
    public void updateChurchMember(String name, Cell cell, Gathering gathering, Position position, Long householderId, String relationshipWithHouseHolder, Integer birthYear, Integer salvationYear, Integer salvationMonth, Integer salvationDay, String carNumber, Gender gender, String phoneNumber, String address) {
        this.name = name;
        this.cell = cell;
        this.gathering = gathering;
        this.position = position;
        this.householderId = householderId;
        this.relationshipWithHouseHolder = relationshipWithHouseHolder;
        this.birthYear = birthYear;
        this.salvationYear = salvationYear;
        this.salvationMonth = salvationMonth;
        this.salvationDay = salvationDay;
        this.carNumber = carNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImageName = profileImageName;
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public void updateProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public void updateProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

}
