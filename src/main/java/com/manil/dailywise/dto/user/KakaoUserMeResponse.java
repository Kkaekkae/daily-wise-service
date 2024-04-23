package com.manil.dailywise.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserMeResponse {
    String id;
    String connectedAt;
    String synchedAt;
    KakaoAccount kakaoAccount;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KakaoAccount {
        Boolean nameNeedAgreement;
        String name;
        String phoneNumber;
        String ageRange;
        String birthday;
        String birthdayType;
        Boolean hasGender;
        String gender;
        String email;
        KakaoProfile profile;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class KakaoProfile {
            String nickname;
            String profileImageUrl;
        }
    }
}
