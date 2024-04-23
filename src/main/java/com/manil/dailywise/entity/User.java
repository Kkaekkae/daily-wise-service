package com.manil.dailywise.entity;

import com.manil.dailywise.enums.user.SnsType;
import com.manil.dailywise.util.UidUtil;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users",
        indexes = {@Index(columnList="created_at")},
        uniqueConstraints = {@UniqueConstraint(columnNames="email")})
@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @Length(max = 8, min=8)
    private String id;

    @Column(unique=true, name = "uniq_id")
    @Length(max = 20, min=6)
    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/_ ]*$")
    private String uniqId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "is_writer")
    private Boolean isWriter = false;

    @Column(name = "is_master_piece")
    private Boolean isMasterPiece = false;

    @Column(name = "profile_image")
    private String profileImage;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "update_wise_time")
    private String updateWiseTime = "08:00";

    @NotNull
    @Enumerated(EnumType.STRING)
    private SnsType provider;

    private String providerId;

    @Column(name = "agree_push")
    private Boolean agreePush = false;

    @OneToMany(mappedBy="to", fetch = FetchType.LAZY)
    private List<Follow> followers;

    @OneToMany(mappedBy="from", fetch = FetchType.LAZY)
    private List<Follow> following;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    private List<Like> likes;

    @Column(name = "borned_at")
    private LocalDateTime bornedAt;

    @Column(name = "deathed_at")
    private LocalDateTime deathedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt = LocalDateTime.now();

    public User() {

    }


    public static User create(
            String name,
            String nickname,
            String profileImage,
            String email,
            SnsType provider,
            String providerId
    ) {
        String uniqueId = UidUtil.generateUid();
        return User.builder()
                .id(UidUtil.generateUid())
                .uniqId(uniqueId)
                .name(name != null ? name : nickname)
                .nickname(nickname)
                .profileImage(profileImage)
                .email(email)
                .provider(provider)
                .verified(true)
                .providerId(providerId)
                .build();
    }

    public void update(
            String nickname,
            Boolean agreePush,
            String profileImage
    ) {
       this.nickname = nickname;
       this.agreePush = agreePush;
       this.profileImage = profileImage;
    }

    public void setWiseTime(String value) {
        this.updateWiseTime = value;
    }

    public void updateAgreePush(Boolean value) {
        this.agreePush  =value;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public static User anonymous() {
        return User.builder().nickname("출처없음").build();
    }
}
