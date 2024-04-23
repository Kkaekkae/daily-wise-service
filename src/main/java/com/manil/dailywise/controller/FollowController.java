package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.ListItemResponse;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.follow.FollowReqDTO;
import com.manil.dailywise.dto.follow.FollowRespDTO;
import com.manil.dailywise.dto.wise.WiseRespDTO;
import com.manil.dailywise.enums.common.SortValue;
import com.manil.dailywise.enums.wise.WiseListSortType;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.FollowService;
import com.manil.dailywise.service.WiseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping(value = "/follow" )
@Slf4j
public class FollowController {
    private FollowService followService;

    @Autowired
    public void setFollowService(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    @ApiOperation(value = "팔로우", notes = "해당 유저를 팔로우 합니다 response 에는 업데이트 이후 상태를 봅니다")
    public ResponseEntity<SingleItemResponse<FollowRespDTO>> follow(
            @RequestBody FollowReqDTO dto,
            @CurrentUser UserPrincipal currentUser
        ) throws KkeaException {
        FollowRespDTO response = followService.follow(dto.getTargetId(), currentUser);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }
}
