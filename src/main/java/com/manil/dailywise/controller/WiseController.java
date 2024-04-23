package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.PageableParam;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.wise.WiseDetailRespDTO;
import com.manil.dailywise.enums.common.SortValue;
import com.manil.dailywise.enums.wise.WiseListSortType;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.WiseService;
import com.manil.dailywise.dto.common.ListItemResponse;
import com.manil.dailywise.dto.wise.WiseRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wise" )
@Slf4j
public class WiseController {
    private WiseService wiseService;

    @Autowired
    public void setWiseService(WiseService wiseService) {
        this.wiseService = wiseService;
    }

    @ApiOperation(value = "명언 검색", notes = "키워드에 따라 명언을 검색합니다.")
    @GetMapping
    public ResponseEntity<ListItemResponse<WiseRespDTO>> getWises(
            @RequestParam(required = false) String search,
            @CurrentUser UserPrincipal currentUser,
            PageableParam pageableParam
    ) throws KkeaException {
        List<WiseRespDTO> response = wiseService.getWises(search,currentUser);
        return ResponseEntity.ok(ListItemResponse.create(response,pageableParam));
    }

    @ApiOperation(value = "특정 유저의 명언 목록 불러오기", notes = "현재 활성화 된 특정 유저의 명언 목록을 가져옵니다.")
    @GetMapping("/user/{userUniqId}")
    public ResponseEntity<ListItemResponse<WiseRespDTO>> getWise(
            @RequestParam(required = false) WiseListSortType sortType,
            @RequestParam(required = false) SortValue sort,
            @PathVariable String userUniqId,
            @CurrentUser UserPrincipal currentUser,
            PageableParam pageableParam
    ) throws KkeaException {
        List<WiseRespDTO> response = wiseService.getWises4User(userUniqId,sortType,sort,currentUser);
        return ResponseEntity.ok(ListItemResponse.create(response,pageableParam));
    }

    @ApiOperation(value = "오늘의 명언 불러오기", notes = "오늘의 명언을 불러옵니다.")
    @GetMapping("/today")
    public ResponseEntity<SingleItemResponse<WiseRespDTO>> getTodayWise(
            @CurrentUser UserPrincipal currentUser
    ) throws KkeaException {
        WiseRespDTO response = wiseService.getTodayWise(currentUser);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @ApiOperation(value = "단일 명언 불러오기", notes = "id 에 따른 명언 상세 정보를 불러옵니다.")
    @GetMapping("/{wiseId}")
    public ResponseEntity<SingleItemResponse<WiseDetailRespDTO>> getWiseDetail(
            @PathVariable String wiseId,
            @CurrentUser UserPrincipal currentUser
    ) throws KkeaException {
        WiseDetailRespDTO response = wiseService.getWiseWithId(wiseId, currentUser);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @ApiOperation(value = "오늘의 명언 갱신 스케쥴링 실행", notes = "해당 API 호출 시 해당 시간으로 설정한 유저의 명언을 갱신합니다.")
    @GetMapping("/schedule")
    public ResponseEntity<SingleItemResponse<WiseRespDTO>> todayWiseSchedule(
    ) throws KkeaException {
        wiseService.updateTodayWiseSchedule();
        return null;
    }
}
