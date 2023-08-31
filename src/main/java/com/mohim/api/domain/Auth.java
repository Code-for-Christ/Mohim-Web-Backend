package com.mohim.api.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auth implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원 아이디

    private String email; // 회원 이메일

    @Column(name = "hashed_password")
    private String hashedPassword; // 해시된 비밀번호

    @Column(name = "temporary_code")
    private String temporaryCode; // 임시코드

    @Column(name = "church_id")
    private Long churchId; // 교회 아이디

    @Column(name = "church_member_id")
    private Long churchMemberId; // 교회멤버 아이디

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일, 후 baseEntity 적용 고려

    @Column(name = "updated_at")
    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일

    @Column(name = "deleted_at")
    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime deletedAt = LocalDateTime.now(); // 삭제일

    @OneToMany(mappedBy = "auth")
    private List<AuthRoleAssociation> authRoleAssociations;

    @Transient
    private List<SimpleGrantedAuthority> authorities;

    @Builder
    public Auth(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public static Auth createAuth(String email, String hashedPassword){
        return Auth.builder()
                .email(email)
                .hashedPassword(hashedPassword)
                .build();
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
