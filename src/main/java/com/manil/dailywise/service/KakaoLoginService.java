package com.manil.dailywise.service;

import com.manil.dailywise.auth.jwt.TokenProvider;
import com.manil.dailywise.dto.user.KakaoAuthTokenResponse;
import com.manil.dailywise.dto.user.KakaoUserMeResponse;
import com.manil.dailywise.dto.user.TokenRespDTO;
import com.manil.dailywise.entity.AuthProvider;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.enums.user.SnsType;
import com.manil.dailywise.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoLoginService {
    private final String clientId;
    private final String kakaoTokenUri;
    private final String callbackUri;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public KakaoLoginService(
            @Value("${login.auth.kakao.clientId}") String clientId,
            @Value("${login.auth.kakao.tokenUri}") String kakaoTokenUri,
            @Value("${login.auth.kakao.callbackUri}") String callbackUri, UserRepository userRepository, TokenProvider tokenProvider) {
        this.clientId = clientId;
        this.kakaoTokenUri = kakaoTokenUri;
        this.callbackUri = callbackUri;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public String authorize() {
        return UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", kakaoTokenUri)
                .queryParam("response_type", "code")
                .queryParam("prompt", "select_account")
                .build().toUriString();
    }

    public KakaoAuthTokenResponse getKakaoAccessToken(String code) {
        String requestUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        // Set Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        // Set parameter
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("code", code);
        // Set http entity

        return restTemplate.postForEntity(requestUrl, new HttpEntity<LinkedMultiValueMap<String, String>>(params, headers), KakaoAuthTokenResponse.class).getBody();
    }

    public KakaoUserMeResponse searchUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new RestTemplate().postForEntity("https://kapi.kakao.com/v2/user/me",
                new HttpEntity<>(headers),
                KakaoUserMeResponse.class).getBody();
    }

    @SneakyThrows
    @Transactional
    public String kakaoUserInsert(String code) {
        KakaoAuthTokenResponse token = getKakaoAccessToken(code);
        KakaoUserMeResponse userInfo = searchUser(token.getAccessToken());
        TokenRespDTO serviceToken = userRepository.findByProviderIdAndProvider(userInfo.getId(), SnsType.KAKAO).map(user -> {
            TokenRespDTO response = new TokenRespDTO();
            response.setToken(tokenProvider.createToken(user));
            return response;
        }).orElseGet(() -> {
            User user = userRepository.save(User.create(
                    userInfo.getKakaoAccount().getName(),
                    userInfo.getKakaoAccount().getProfile().getNickname(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                    userInfo.getKakaoAccount().getEmail(),
                    SnsType.KAKAO,
                    userInfo.getId()
            ));
            TokenRespDTO response = new TokenRespDTO();
            response.setToken(tokenProvider.createToken(user));
            return response;
        });
        return UriComponentsBuilder.fromHttpUrl(callbackUri)
                .queryParam("token", serviceToken.getToken()).build().toUriString();
    }
}
