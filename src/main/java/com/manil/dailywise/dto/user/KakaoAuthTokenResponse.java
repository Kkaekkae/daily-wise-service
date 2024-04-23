package com.manil.dailywise.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
public class KakaoAuthTokenResponse {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String idToken;
    private Integer expiresIn;
    private String scope;
    private Integer refreshTokenExpiresIn;
    private String error;
    private String errorDescription;
    private String errorCode;
}
