package com.manil.dailywise.controller;

import com.manil.dailywise.dto.user.TokenRespDTO;
import com.manil.dailywise.service.KakaoLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/user/auth/kakao")
@Slf4j
class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }


    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoLoginService.authorize());
    }

    @GetMapping("/token")
    public void getToken(@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoLoginService.kakaoUserInsert(code));
    }
}