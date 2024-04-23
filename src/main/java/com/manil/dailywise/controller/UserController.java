package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.ListItemResponse;
import com.manil.dailywise.dto.common.PageableParam;
import com.manil.dailywise.dto.user.*;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.UserService;
import com.manil.dailywise.dto.common.SingleItemResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user" )
@Slf4j
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SingleItemResponse<UserProfileDTO>> getCurrentUser(
            @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        UserProfileDTO myInfo = userService.getMyInformation(userPrincipal.getId());
        return ResponseEntity.ok(SingleItemResponse.create(myInfo));
    }

    @GetMapping
    @ApiOperation(value = "유저 검색", notes = "필터를 통해 유저를 찾습니다. 필터는 유저 ID, 이름, 명언 이름이 검색됩니다.")
    public ResponseEntity<ListItemResponse<UserProfileDTO>> getUsers(
            @RequestParam String search,
            @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal,
            PageableParam pageableParam) throws KkeaException {
        List<UserProfileDTO> response = userService.getUsers(search, userPrincipal);
        return ResponseEntity.ok(ListItemResponse.create(response,pageableParam));
    }

    @GetMapping("/{userUniqId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SingleItemResponse<UserProfileDTO>> getUserProfile(
            @PathVariable String userUniqId,
            @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        UserProfileDTO response = userService.getUser(userUniqId, userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원 가입", notes = "회원 가입 API, SNS 가입 이후 쿼리 파람으로 전달받는 유저의 ID 를 userId 로 넣어주세요.")
    ResponseEntity<SingleItemResponse<TokenRespDTO>> joinUser(
            @RequestBody JoinReqDTO dto) throws KkeaException {
        TokenRespDTO response = userService.join(dto);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 API")
    ResponseEntity<SingleItemResponse<TokenRespDTO>> login(
            @RequestBody LoginReqDTO dto) throws KkeaException {
        TokenRespDTO response = userService.login(dto);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @PutMapping
    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보 수정 API")
    ResponseEntity<SingleItemResponse<UserProfileDTO>> updateUser(
            @RequestBody UserUpdateReqDTO dto,
            @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        UserProfileDTO response = userService.updateUser(dto,userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @PutMapping("/time")
    @ApiOperation(value = "알림 발송 시간 변경", notes = "알림 발송 시간 변경 API")
    ResponseEntity<SingleItemResponse<UserProfileDTO>> updatePushTime(
            @RequestBody UserTimeUpdateReqDTO dto,
            @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        UserProfileDTO response = userService.updateUserPushTime(dto,userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @PutMapping("/time/active")
    @ApiOperation(value = "알림 발송 여부 변경", notes = "알림 발송 여부 변경 API")
    ResponseEntity<SingleItemResponse<UserProfileDTO>> updatePushActive(
            @RequestBody UserActivePushUpdateReqDTO dto,
            @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        UserProfileDTO response = userService.updateUserPushActive(dto,userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }


    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴 API")
    @DeleteMapping("/withdraw")
    public ResponseEntity<SingleItemResponse<String>> withDraw(
            @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        log.info("withDraw§request§userId={}", userPrincipal.getId());
        userService.withDraw(userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(""));
    }

    @ApiOperation(value = "회원 접속 체크", notes = "회원의 접속 시간을 저장합니다.")
    @PostMapping("/connect")
    public ResponseEntity<SingleItemResponse<String>> userConnect(
            @CurrentUser UserPrincipal userPrincipal) throws KkeaException {
        log.info("connect§request§userId={}",userPrincipal.getId());
        userService.updateLastLogin(userPrincipal);
        return ResponseEntity.ok(SingleItemResponse.create(""));
    }

}
