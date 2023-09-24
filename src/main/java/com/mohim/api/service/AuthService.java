package com.mohim.api.service;

import com.mohim.api.domain.*;
import com.mohim.api.dto.*;
import com.mohim.api.exception.CustomException;
import com.mohim.api.exception.ErrorCode;
import com.mohim.api.repository.AuthRepository;
import com.mohim.api.repository.ChurchRepository;
import com.mohim.api.repository.MemberRepository;
import com.mohim.api.security.JwtTokenProvider;
import com.mohim.api.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final ChurchRepository churchRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;
    private final CodeGenerator codeGenerator;



    public void join(AuthJoinRequest authJoinRequest) {
        Auth auth = Auth.createAuth(authJoinRequest.getEmail(), passwordEncoder.encode(authJoinRequest.getPassword()));
        authRepository.save(auth);
    }


    public Auth getUser(Long id) {
        return authRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found user with id =" + id));
    }

    public Optional<Auth> getUser(String email) {
        return authRepository.findByEmail(email);
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        Optional<Auth> auth = authRepository.findByEmail(request.getEmail());

        if(auth.isEmpty()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ID);
        }
        if(!passwordEncoder.matches(request.getPassword(), auth.get().getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(auth.get());
        ChurchMember churchMember = memberRepository.findById(auth.get().getChurchMemberId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        AuthLoginResponse response = AuthLoginResponse.builder()
                .name(churchMember.getName())
                .churchId(auth.get().getChurch().getId())
                .churchName(auth.get().getChurch().getName())
                .churchMemberId(auth.get().getChurchMemberId())
                .accessToken(accessToken)
                .email(auth.get().getEmail())
                .role(auth.get().getAuthRoleAssociations().stream()
                        .map(AuthRoleAssociation::getRole)
                        .map(Role::getName)
                        .findFirst() // 첫 번째 요소 가져오기
                        .orElse("")) // 없다면 빈 스트링
                .build();

        return response;

    }

    @Transactional
    public AuthAuthenticateResponse authenticate(AuthAuthenticateRequest request, Long authId) {

        Church church = churchRepository.findById(request.getChurchId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CHURCH));
        ChurchMember churchMember = memberRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHURCH_MEMBER));
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Optional<Auth> existedAuthorizedUser = authRepository.findByChurchMemberId(churchMember.getId());

        if (existedAuthorizedUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_CHURCH_MEMBER);
        }

        auth.setChurch(church);
        auth.setChurchMemberId(churchMember.getId());

        authRepository.save(auth);

        AuthAuthenticateResponse response = AuthAuthenticateResponse.builder()
                .id(auth.getId())
                .email(auth.getEmail())
                .churchId(church.getId())
                .churchMemberId(churchMember.getId())
                .isAuthenticated(true)
                .build();

        return response;
    }

    public AuthAuthenticateResponse getAuthenticationStatus(Auth authInfo) {
        Auth auth = authRepository.findById(authInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        boolean isAuthenticated = auth.getChurch() != null && auth.getChurchMemberId() != null;

        return AuthAuthenticateResponse.builder()
                .email(auth.getEmail())
                .id(auth.getId())
                .churchId(auth.getChurch().getId())
                .churchMemberId(auth.getChurchMemberId())
                .isAuthenticated(isAuthenticated)
                .build();
    }

    public AuthRefreshTokenResponse refreshToken(Auth authInfo) {
        String token = jwtTokenProvider.generateAccessToken(authInfo);
        return AuthRefreshTokenResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public AuthFindPasswordResponse findPassword(String email) {
        Auth auth = authRepository.findByEmail(email).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_EMAIL));

        String temporaryCode = codeGenerator.generateEightCharacterCode();

        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("JBCH Phonebook 비밀번호 찾기")
                .message("임시코드: " + temporaryCode)
                .build();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(emailMessage.getMessage()); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }

        auth.setTemporaryCode(temporaryCode);
        authRepository.save(auth);

        return AuthFindPasswordResponse.builder()
                .message("임시 코드를 성공적으로 발송하였습니다.")
                .build();
    }

    @Transactional
    public AuthChangePasswordResponse changePassword(AuthChangePasswordRequest request) {

        Auth auth = authRepository.findByEmail(request.getEmail()).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!auth.getTemporaryCode().equals(request.getTemporaryCode())){
            throw new CustomException(ErrorCode.INVALID_CODE);
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        auth.setHashedPassword(hashedPassword);
        auth.setTemporaryCode(null);

        authRepository.save(auth);

        return AuthChangePasswordResponse.builder()
                .message("비밀번호가 성공적으로 변경되었습니다.")
                .build();
    }

    public AuthDeleteUserResponse deleteUser(Auth authInfo, String email) {
        Auth auth = authRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!authInfo.getEmail().equals(email)){
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM);
        }

        authRepository.delete(auth);

        return AuthDeleteUserResponse.builder()
                .message("계정이 삭제되었습니다.")
                .build();
    }
}
