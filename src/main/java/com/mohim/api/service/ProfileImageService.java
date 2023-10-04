package com.mohim.api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mohim.api.domain.ChurchMember;
import com.mohim.api.dto.ProfileImageUrlResponse;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final MemberRepository memberRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String getProfileImageUrl(Long churchId, Long memberId) {
        ChurchMember churchMember = memberRepository.findByIdAndChurchId(memberId, churchId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));

        String preSignedURL = "";
        String fileName = churchMember.getProfileImageName();

        if (fileName == null) {
            return null;
        }

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);

        log.info(expiration.toString());

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, fileName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration)
                            .withKey(churchId + "/" + fileName);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            preSignedURL = url.toString();
            log.info("Pre-Signed URL : " + url.toString());

        } catch (Exception e) {
            log.error("URL 가져오기 실패 : " + e);
        }

        return preSignedURL;
    }

    public void uploadProfileImage(ChurchMember churchMember, MultipartFile multipartFile, String fileType) throws IOException {
        // 1. 기존 프로필 이미지 정보 조회
        String profileImageToBeDeleted = churchMember.getProfileImageName();

        // 2. 썸네일 만들기
        String thumbnail = createThumbnail(multipartFile, fileType);

        // 3. 새 이미지 s3 업로드
        // 3-1. 파일명 중복을 방지하기 위한 UUID 추가
        String profileImageName = UUID.randomUUID().toString();

        // 3-2. 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 3-3. 업로드
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String key = churchMember.getChurch().getId() + "/" + profileImageName + "." + fileType;
            amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata));
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new CustomException(ErrorCode.UPLOAD_FAILED); // 파일 업로드 실패 에러를 던짐}
        }

        // 4. 새로운 프로필 이미지 DB 반영
        churchMember.updateProfileImageName(profileImageName + "." + fileType);
        // 5. 썸네일 이미지 DB 반영
        churchMember.updateProfileImageThumbnail(thumbnail);

        // 6. 기존 프로필 이미지 삭제
        // TODO TEST 시 주석 처리
//        if (profileImageToBeDeleted != null) {
//            String key = churchMember.getChurch().getId() + "/" + profileImageToBeDeleted;
//            amazonS3.deleteObject(bucket, key);
//        }
    }

    public void deleteProfileImage(ChurchMember churchMember) {
        String profileImageToBeDeleted = churchMember.getProfileImageName();

        if (profileImageToBeDeleted != null) {
            // 기존 프로필 이미지 삭제
            churchMember.updateProfileImageName(null);
            // 기존 썸네일 삭제
            churchMember.updateProfileImageThumbnail(null);

            // s3 삭제
            String key = churchMember.getChurch().getId() + "/" + profileImageToBeDeleted;
            amazonS3.deleteObject(bucket, key);
        }
    }

    private String createThumbnail(MultipartFile file, String fileType) throws IOException {
        try {
            // MultipartFile에서 이미지 데이터 읽기
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // 원본 이미지의 가로와 세로 크기
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 원본 이미지의 가로와 세로 비율 계산
            double aspectRatio = (double) originalWidth / originalHeight;

            // 썸네일의 크기 설정
            int thumbnailWidth;
            int thumbnailHeight;

            // 가로가 세로보다 긴 경우
            if (originalWidth > originalHeight) {
                thumbnailWidth = 250;
                thumbnailHeight = (int) (thumbnailWidth / aspectRatio);
            } else { // 세로가 가로보다 긴 경우
                thumbnailHeight = 250;
                thumbnailWidth = (int) (thumbnailHeight * aspectRatio);
            }


//            // 이미지를 250x250 크기로 조정
//            int thumbnailWidth = 250;
//            int thumbnailHeight = 250;
            BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = thumbnail.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
            graphics2D.dispose();

            // 조정된 이미지를 Base64로 인코딩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, fileType, baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Thumbnail = Base64.getEncoder().encodeToString(imageBytes);

            return base64Thumbnail;
        } catch (IOException e) {
            // 오류 처리
            e.printStackTrace();
            throw e;
        }
    }

}
