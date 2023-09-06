package com.mohim.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.ProfileImageUrlResponse;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final MemberRepository memberRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public ProfileImageUrlResponse getProfileImageUrl(Integer churchId, Integer memberId) {
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(Long.valueOf(memberId), Long.valueOf(churchId)).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));

        String imageUrl = amazonS3.getUrl(bucket, churchMember.getProfileImageName()).toString();

        if (!imageUrl.contains(".jpg")) {
            throw new CustomException(ErrorCode.NOT_FOUND_PROFILE_IMAGE);
        }

        return ProfileImageUrlResponse.builder()
                .profileImageUrl(imageUrl)
                .build();
    }
}
